"use strict";

////////////////////////////////////////////////////////////////////////////////////////
//
// cs411 assignment 5 - surface rendering
//
/////////////////////////////////////////////////////////////////////////////////////////

var canvas;
var gl;
var buffers;                        // vertex buffers
var model;                          // object model

var lastAnimationTime = Date.now(); // last time tick() was called
var angleStep = 10;                 // increments of rotation angle (degrees)
var fps = 30;                       // frames per second
var currentAngle = 0.0;             // current rotation angle [degree]

//var objName = 'https://raw.githubusercontent.com/cs411iit/public/master/mycube.obj';
var objName = 'https://raw.githubusercontent.com/cs411iit/public/master/cow.obj';

var camZ = 0;
var invertNorm = true;
var curRot = new Matrix4();
var leftRot = new Matrix4();
var rightRot = new Matrix4();
var upRot = new Matrix4();
var downRot = new Matrix4();
var tmpRot = new Matrix4();
var cmMatrix = new Matrix4();
var lightPos = new Vector4();
var laConst = [ 0, 0.5, 0.5 ];
var rConst = {
    ambient: 1,
    diffuse: 1,
    specular: 1,
    shiny: 4,
};

// vertex shader program
var VSHADER_SOURCE = `
    attribute vec4 a_Position;
    attribute vec4 a_Color;
    attribute vec4 a_Normal;
    uniform mat4 u_MvpMatrix;
    uniform mat4 u_MvMatrix;
    uniform mat4 u_NormalMatrix;
    uniform vec4 u_LightPosition;
    varying vec4 v_Color;

    uniform float u_la_kc;
    uniform float u_la_kl;
    uniform float u_la_kq;

    uniform float u_shiny;
    uniform float u_ambient;
    uniform float u_specular;
    uniform float u_diffuse;

    float attenuate(vec3 pos, vec3 ls) {
        float d = distance(a_Position.xyz, u_LightPosition.xyz);
        return 1.0 / (u_la_kc + u_la_kl * d + u_la_kq * d * d);
    }
    vec3 subtract(vec4 v1, vec4 v2) {
        return vec3(v1.x - v2.x, v1.y - v2.y, v1.z - v2.z);
    }

    void main() {
        vec3 cPos = (vec4(20, 0, 0, 1) * u_MvpMatrix).xyz;
        vec3 pos = (u_MvpMatrix * a_Position).xyz;
        gl_Position = u_MvpMatrix * a_Position;
        vec3 lpos = (u_MvpMatrix * u_LightPosition).xyz;
        vec3 L = normalize(subtract(u_MvpMatrix * u_LightPosition, a_Position));
        vec3 N = normalize(vec3(u_NormalMatrix * a_Normal));

        // testing
        // vec3 aClr = vec3(1.0, 0.0, 0.0);
        // vec3 sClr = vec3(0.0, 1.0, 0.0);
        // vec3 dClr = vec3(0.0, 0.0, 1.0);

        vec3 aClr = vec3(1.0, 1.0, 1.0);
        vec3 sClr = vec3(1.0, 1.0, 1.0);
        vec3 dClr = vec3(1.0, 1.0, 1.0);

        float specular = 0.0;

        // Lambert's cosine law
        float lam = max(dot(N, L), 0.0);
        if (lam < 0.0) {
            vec3 R = normalize(reflect(-L, N));
            vec3 V = normalize(pos - cPos);

            // shininess
            float sAng = max(dot(R, V), 0.0);
            specular = pow(sAng, u_shiny);
        }
        float att = attenuate(pos.xyz, lpos);
        vec3 clr = u_ambient * aClr +
            u_diffuse * lam * dClr * att +
            specular * sClr;

        v_Color = vec4(clr * a_Color.rgb, a_Color.a);

    }`;

// fragment shader program
var FSHADER_SOURCE = `
  #ifdef GL_ES
  precision mediump float;
  #endif

  varying vec4 v_Color;

  void main() {
    gl_FragColor = v_Color;
  }
`;

// event handlers

function turnLeft()
{
  tmpRot.set(leftRot);
  tmpRot.multiply(curRot);
  curRot.set(tmpRot);
}

function turnRight()
{
  tmpRot.set(rightRot);
  tmpRot.multiply(curRot);
  curRot.set(tmpRot);
}

function turnUp()
{
  tmpRot.set(upRot);
  tmpRot.multiply(curRot);
  curRot.set(tmpRot);
}

function turnDown()
{
  tmpRot.set(downRot);
  tmpRot.multiply(curRot);
  curRot.set(tmpRot);
}

function zoomIn()
{
  camZ+=1;
}

function zoomOut()
{
  if (camZ >= 0)
    camZ-=1;
}


function invertNormals()
{
  invertNorm = !invertNorm;
  readOBJFile(objName, gl, model, 60, invertNorm)
    .then(() => {
      initModel();
      drawScene(gl, gl.program, undefined, buffers, model)
    })
    .catch(console.error);
}


// create a buffer object, assign it to attribute variable, and enable the assignment
function createEmptyArrayBuffer(gl, a_attribute, num, type)
{
  var buffer =  gl.createBuffer();  // Create a buffer object
  if (!buffer) {
    console.log('Failed to create the buffer object');
    return null;
  }
  gl.bindBuffer(gl.ARRAY_BUFFER, buffer);
  gl.vertexAttribPointer(a_attribute, num, type, false, 0, 0);  // Assign the buffer object to the attribute variable
  gl.enableVertexAttribArray(a_attribute);  // Enable the assignment

  return buffer;
}


function initVertexBuffers(gl, program)
{
  var o = new Object(); // create new object. Utilize Object object to return multiple buffer objects
  o.vertexBuffer = createEmptyArrayBuffer(gl, program.a_Position, 3, gl.FLOAT);
  o.normalBuffer = createEmptyArrayBuffer(gl, program.a_Normal, 3, gl.FLOAT);
  o.colorBuffer = createEmptyArrayBuffer(gl, program.a_Color, 4, gl.FLOAT);
  o.indexBuffer = gl.createBuffer();
  if (!o.vertexBuffer || !o.normalBuffer || !o.colorBuffer || !o.indexBuffer) { return null; }

  gl.bindBuffer(gl.ARRAY_BUFFER, null);

  return o;
}


function assignVertexBuffersData(gl, buffers, model)
{
  // write date into the buffer objects
  gl.bindBuffer(gl.ARRAY_BUFFER, buffers.vertexBuffer);
  gl.bufferData(gl.ARRAY_BUFFER, model.arrays.vertices, gl.STATIC_DRAW);

  gl.bindBuffer(gl.ARRAY_BUFFER, buffers.normalBuffer);
  gl.bufferData(gl.ARRAY_BUFFER, model.arrays.normals, gl.STATIC_DRAW);

  gl.bindBuffer(gl.ARRAY_BUFFER, buffers.colorBuffer);
  gl.bufferData(gl.ARRAY_BUFFER, model.arrays.colors, gl.STATIC_DRAW);

  gl.bindBuffer(gl.ELEMENT_ARRAY_BUFFER, buffers.indexBuffer);
  gl.bufferData(gl.ELEMENT_ARRAY_BUFFER, model.arrays.indices, gl.STATIC_DRAW);
}


function getShaderVariables(program)
{
  //get the storage locations of attribute and uniform variables
  program.a_Position = gl.getAttribLocation(program, 'a_Position');
  program.a_Normal = gl.getAttribLocation(program, 'a_Normal');
  program.a_Color = gl.getAttribLocation(program, 'a_Color');
  program.u_MvpMatrix = gl.getUniformLocation(program, 'u_MvpMatrix');
  program.u_NormalMatrix = gl.getUniformLocation(program, 'u_NormalMatrix');
  program.u_LightPosition = gl.getUniformLocation(program, 'u_LightPosition');
  program.u_la_kc = gl.getUniformLocation(program, 'u_la_kc');
  program.u_la_kl = gl.getUniformLocation(program, 'u_la_kl');
  program.u_la_kq = gl.getUniformLocation(program, 'u_la_kq');
  program.u_ambient = gl.getUniformLocation(program, 'u_ambient');
  program.u_specular = gl.getUniformLocation(program, 'u_specular');
  program.u_diffuse = gl.getUniformLocation(program, 'u_diffuse');

  console.log(program);
  /*
  console.log("loaded shader vars: ", {
      program.a_Position,
      program.a_Normal,
      program.a_Color
  })
  */
  if (program.a_Position < 0 ||  program.a_Normal < 0 || program.a_Color < 0 ||
      !program.u_MvpMatrix || !program.u_NormalMatrix) {
    console.log('attribute, uniform');
    return false;
  }
  return true;

}

function printModelInfo(model)
{
  console.log("number of vertices=%d",model.arrays.vertices.length/3);
  console.log("number of normals=%d",model.arrays.normals.length/3);
  console.log("number of colors=%d",model.arrays.colors.length/4);
  console.log("nummer of faces=%d",model.arrays.indices.length/3);

  for(var i=0;i<10 && i< model.arrays.vertices.length; i++){
    console.log("v[%d]=(%f,%f,%f)",i,
      model.arrays.vertices[i*3+0],
      model.arrays.vertices[i*3+1],
      model.arrays.vertices[i*3+2]);
  }
  for(var i=0;i<10 && i< model.arrays.vertices.length; i++){
    console.log("n[%d]=(%f,%f,%f)",i,
      model.arrays.normals[i*3+0],
      model.arrays.normals[i*3+1],
      model.arrays.normals[i*3+2]);
  }
  for(var i=0;i<10 && i< model.arrays.indices.length; i++){
    console.log("f[%d]=(%d,%d,%d)",i,
      model.arrays.indices[i*3+0],
      model.arrays.indices[i*3+1],
      model.arrays.indices[i*3+2]);
  }
  for(var i=0;i<10 && i< model.arrays.colors.length; i++){
    console.log("c[%d]=(%d,%d,%d)",i,
      model.arrays.colors[i*3+0],
      model.arrays.colors[i*3+1],
      model.arrays.colors[i*3+2]);
  }
}

function printValues() {
    console.console.log({
        lightingConstants: { kc: laConst[0], kl: laConst[1], kq: laConst[2], },
        lightPosition: lightPos,
    });
}
function initModel() {
  // get model arrays if necessary
  if (!model.arrays) {
    if(isOBJFileLoaded(model)) {
      extractOBJFileArrays(model);
      //printModelInfo(model);
    }
    if (!model.arrays) {
      console.error("drawing failed!", model);
      return;   // drawing failed
    }
  }

  // set all normals to zero
  model.objDoc.normals = model.objDoc.vertices.map(m => ({x: 0, y: 0, z: 0}));
  // add face normals to each component vertex
  model.objDoc.objects[0].faces.forEach(f =>
    f.vIndices.forEach(vi => {
      model.objDoc.normals[vi].x += f.normal.x;
      model.objDoc.normals[vi].y += f.normal.y;
      model.objDoc.normals[vi].z += f.normal.z;
    })
  );

  // normalize vertex normals
  model.objDoc.normals = model.objDoc.normals.map(n => {
    const l = Math.sqrt(Math.pow(n.x, 2) + Math.pow(n.y, 2) + Math.pow(n.z, 2));
    return { x: n.x / l, y: n.y / l, z: n.z / l };
  });

  let nArr = [];
  model.objDoc.objects[0].faces.forEach(f =>
    f.vIndices.forEach(vi => {
      const n = model.objDoc.normals[vi];
      nArr.push(n.x, n.y, n.z);
    })
  );
  model.arrays.normals = new Float32Array(nArr);


  // normalize verticies according to bounding box
  const max = model.arrays.vertices.reduce(
    (max, cur) => Math.max(max, Math.abs(cur)), 0);
  model.arrays.vertices = model.arrays.vertices.map((v, i) => v / max);

  // calculate center of mass
  let cm = [ 0,0,0 ];
  model.arrays.vertices.forEach((c, i) => cm[i % 3] += c);
  // negate and average
  cm = cm.map(c => - c / model.arrays.vertices.length);
  console.log({ cm });
  cmMatrix.setTranslate(cm[0], cm[1], cm[2]);

  assignVertexBuffersData(gl, buffers, model);

}

function initScene()
{
  // set the clear color and enable the depth test
  gl.clearColor(0.2, 0.2, 0.2, 1.0);
  gl.enable(gl.DEPTH_TEST);

  // select the viewport
  gl.viewportWidth = canvas.width;
  gl.viewportHeight = canvas.height;
  gl.viewport(0, 0, gl.viewportWidth, gl.viewportHeight);

  // set the projection matrix
  pMatrix.setPerspective(30.0, canvas.width/canvas.height, 1.0, 5000.0);

  // set the modelview matrix
  mvMatrix.setIdentity(); // erase all prior transformations
  mvMatrix.lookAt(20, 0, 0,   0.0, 0.0, 0.0,   0.0, 1.0, 0.0);

  // start reading the OBJ file
  model = new Object();
  var scale=60; // 1
  readOBJFile(objName, gl, model, scale, true)
  .then(() => {
      initModel();
  }).catch(console.error); // cube.obj

  // init rotation matrices
  curRot.setIdentity();
  leftRot .setRotate( 5, 0,1,0);
  rightRot.setRotate(-5, 0,1,0);
  upRot   .setRotate(-5, 0,0,1);
  downRot .setRotate( 5, 0,0,1);

  lightPos = new Vector4([1,1,1, 1]);

}


function drawScene(gl, program, angle, buffers, model)
{
  // get model arrays if necessary
  if (!model.arrays)
    return;   // drawing failed

  // clear canvas
  gl.clear(gl.COLOR_BUFFER_BIT | gl.DEPTH_BUFFER_BIT);  // Clear color and depth buffers

  // perform modeling transformations (rotate)
  mvPushMatrix();
  /*
  mvMatrix.rotate(angle, 1.0, 0.0, 0.0); // about x
  mvMatrix.rotate(angle, 0.0, 1.0, 0.0); // about y
  mvMatrix.rotate(angle, 0.0, 0.0, 1.0); // about z
  */
  mvMatrix.multiply(cmMatrix);
  mvMatrix.translate(camZ, 0, 0);
  mvMatrix.multiply(curRot);
  // set the normal matrix
  nMatrix.setInverseOf(mvMatrix);
  nMatrix.transpose();
  gl.uniformMatrix4fv(program.u_NormalMatrix, false, nMatrix.elements);

  // compute the combined transformation matrix
  mvpMatrix.set(pMatrix);
  mvpMatrix.multiply(mvMatrix);
  gl.uniformMatrix4fv(program.u_MvpMatrix, false, mvpMatrix.elements);
  gl.uniformMatrix4fv(program.u_MvMatrix, false, mvMatrix.elements);
  mvPopMatrix();

  gl.uniform1f(program.u_la_kc, laConst[0]);
  gl.uniform1f(program.u_la_kl, laConst[1]);
  gl.uniform1f(program.u_la_kq, laConst[2]);

  gl.uniform1f(program.u_shiny, rConst.shiny);
  gl.uniform1f(program.u_ambient, rConst.ambient);
  gl.uniform1f(program.u_specular, rConst.specular);
  gl.uniform1f(program.u_diffuse, rConst.diffuse);

  // draw
  gl.drawElements(gl.TRIANGLES, model.arrays.indices.length, gl.UNSIGNED_SHORT, 0);

  gl.uniform4fv(program.u_LightPosition, lightPos.elements);

}


function animate(angle)
{
  var now = Date.now();
  var elapsed = now - lastAnimationTime;
  if(elapsed < 1000/fps) return angle;
  lastAnimationTime = now;
  // update the current rotation angle (adjusted by elapsed time)
  var newAngle = angle + (angleStep * elapsed) / 1000.0;
  return newAngle % 360;
}


function tick()
{
  currentAngle = animate(currentAngle); // update current rotation angles
  drawScene(gl, gl.program, currentAngle, buffers, model);
  requestAnimationFrame(tick, canvas);
}


function main() {
  // retrieve the <canvas> element
  canvas = document.getElementById('webgl');

  // get rendering context for WebGL
  gl = getWebGLContext(canvas);
  if (!gl) {
    console.log('Failed to get the rendering context for WebGL');
    return;
  }

  // initialize shaders
  if (!initShaders(gl, VSHADER_SOURCE, FSHADER_SOURCE)) {
    console.log('Failed to intialize shaders.');
    return;
  }

  // get storage locations of attribute and uniform variables
  var program = gl.program;
  if(!getShaderVariables(program)){
    console.log('error locating shader variables');
    return;
  }

  // prepare empty buffer objects
  buffers = initVertexBuffers(gl, program);
  if (!buffers) {
    console.log('Failed to set the vertex information');
    return;
  }


  // set button event listeners
  var turnLeftBtn = document.getElementById('turnLeftBtn');
  turnLeftBtn.addEventListener('click', turnLeft);

  var turnRightBtn = document.getElementById('turnRightBtn');
  turnRightBtn.addEventListener('click', turnRight);

  var turnUpBtn = document.getElementById('turnUpBtn');
  turnUpBtn.addEventListener('click', turnUp);

  var turnDownBtn = document.getElementById('turnDownBtn');
  turnDownBtn.addEventListener('click', turnDown);

  var zoomInBtn = document.getElementById('zoomInBtn');
  zoomInBtn.addEventListener('click', zoomIn);

  var zoomOutBtn = document.getElementById('zoomOutBtn');
  zoomOutBtn.addEventListener('click', zoomOut);

  var invertNormalsBtn = document.getElementById('invertNormalsBtn');
  invertNormalsBtn.addEventListener('click', invertNormals);

  document.getElementById('l-x').oninput = function (){ lightPos.elements[0] = ( this.value - 50) / 50; };
  document.getElementById('l-y').oninput = function (){ lightPos.elements[1] = ( this.value - 50) / 50; };
  document.getElementById('l-z').oninput = function (){ lightPos.elements[2] = ( this.value - 50) / 50; };
  document.getElementById('la-kc').oninput = function () { laConst[0] = (this.value - 50) / 50; };
  document.getElementById('la-kl').oninput = function () { laConst[1] = (this.value - 50) / 50; };
  document.getElementById('la-kq').oninput = function () { laConst[2] = (this.value - 50) / 50; };
  document.getElementById('amb-slide').oninput = function () { rConst.ambient = this.value / 100; };
  document.getElementById('diff-slide').oninput = function () { rConst.diffuse = this.value / 100; };
  document.getElementById('spec-slide').oninput = function () { rConst.specular = this.value / 100; };
  document.getElementById('shin-slide').oninput = function () { rConst.shiny = this.value; };

  // initialize the scene and start animation
  initScene();
  tick();
}


// EOF
