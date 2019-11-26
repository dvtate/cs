"use strict";

/////////////////////////////////////////////////////////////////////////////////////////
//
// cs411 assignment 2 - 2d modeling and viewing
//
/////////////////////////////////////////////////////////////////////////////////////////

// global variables
var canvas;
var gl;
var lastAnimationTime = Date.now();
var fps=30;
var u_ModelMatrix;
var u_FragColor;
var a_Position;
var vertices;
var n;

var speed=0.1;
var angSpeed=1; 
var renderMode=0;
var pauseFlag=0;
let rotFlag= true;
var boardW=10.0;          // board width
var boardH=10.0;          // board height
var curPosX=0,curPosY=0;  // current position of object
var curRotAngle = 0;      // current rotation of object
var dX,dY;                // currect direction of motion (unit vector)
var past = [];


// Vertex shader program
var VSHADER_SOURCE = 
  'attribute vec4 a_Position;\n' +
  'uniform mat4 u_ModelMatrix;\n' +
  'void main() {\n' +
  '  gl_Position = u_ModelMatrix * a_Position;\n' +
  '  gl_PointSize = 3.0;\n' +
  '}\n';

// Fragment shader program
var FSHADER_SOURCE =
  'precision mediump float;\n' +
  'uniform vec4 u_FragColor;\n' +
  'void main() {\n' +
  '  gl_FragColor = u_FragColor;\n' + 
  '}\n';


// button event handlers
function speedUp() {
  speed *= 2;
  angSpeed += 1;
  console.log('speed = %f, angSpeed = %f', speed, angSpeed);
}

function speedDown() {
  speed /= 2;
  angSpeed -= 1;
  if (speed<0.0001) speed=0.0001;
  if (angSpeed<1) angSpeed=1;
  console.log('speed = %f, angSpeed = %f', speed, angSpeed);
}

function zoomIn() {
  //////////////////////////////////////////////////////////////////////////////////
  //////////////////////////////// YOUR CODE HERE //////////////////////////////////
  //////////////////////////////////////////////////////////////////////////////////
  mvMatrix.scale(1.1, 1.1, 1.1);
}

function zoomOut() {
  //////////////////////////////////////////////////////////////////////////////////
  //////////////////////////////// YOUR CODE HERE //////////////////////////////////
  //////////////////////////////////////////////////////////////////////////////////
  
  mvMatrix.scale(0.9, 0.9, 0.9);
}

function toggleRenderMode() {
  renderMode++; 
  if(renderMode>1) renderMode=0;
  console.log('renderMode = %d', renderMode);
}

function togglePause() {
  pauseFlag = 1-pauseFlag;
  console.log('pauseFlag = %d', pauseFlag);
}

function toggleRotate(){
  rotFlag = !rotFlag;
  console.log({ rotFlag });
}

function initVertexBuffers(gl) 
{
//////////////////////////////////////////////////////////////////////////////////
////////////////////////// DO NOT CHANGE THIS OBJECT /////////////////////////////
//////////////////////////////////////////////////////////////////////////////////
  var vertices = new Float32Array(
    [0,    0.3,   
    -0.3, -0.3,   
     0.3, -0.3,
     0.0,  -0.1]); // CM
  var n = 3; // The number of vertices

  
  // Create a buffer object
  var vertexBuffer = gl.createBuffer();
  if (!vertexBuffer) {
    console.log('Failed to create the buffer object');
    return false;
  }

  // Bind the buffer object to target
  gl.bindBuffer(gl.ARRAY_BUFFER, vertexBuffer);
  // Write data into the buffer object
  gl.bufferData(gl.ARRAY_BUFFER, vertices, gl.STATIC_DRAW);

  var a_Position = gl.getAttribLocation(gl.program, 'a_Position');
  if (a_Position < 0) {
    console.log('Failed to get the storage location of a_Position');
    return -1;
  }
  // Assign the buffer object to a_Position variable
  gl.vertexAttribPointer(a_Position, 2, gl.FLOAT, false, 0, 0);

  // Enable the assignment to a_Position variable
  gl.enableVertexAttribArray(a_Position);

  return n;
}


function initScene(gl,u_ModelMatrix,u_FragColor,n)
{
  // select the viewport
  gl.viewport(0, 0, gl.viewportWidth, gl.viewportHeight);
 
  // reset the modelview matrix
  mvMatrix.setIdentity(); // erase all prior transformations

  // select the view window (projection camera)
  var left=-boardW/2.0, right=boardW/2.0, bottom=-boardH/2.0, top=boardH/2.0, near=0, far=10;
  pMatrix.setIdentity();
  pMatrix.ortho(left,right,bottom,top,near,far);
  mvMatrix.multiply(pMatrix);
  //mvPushMatrix();

  // set the camera position and orientation (viewing transformation)
  var eyeX=0, eyeY=0, eyeZ=10;
  var centerX=0, centerY=0, centerZ=0;
  var upX=0, upY=1, upZ=0;
  mvMatrix.lookAt(eyeX, eyeY, eyeZ, centerX, centerY, centerZ, upX, upY, upZ);
  //mvPushMatrix();
}



function drawScene(gl,u_ModelMatrix,u_FragColor,n)
{

  // clear canvas
  gl.clearColor(0, 0, 0, 1);
  gl.clear(gl.COLOR_BUFFER_BIT);


  mvPushMatrix();

  //////////////////////////////////////////////////////////////////////////////////
  ////////////////////////// TRANSFORM THE OBJECT //////////////////////////////////
  //////////////////////////////////////////////////////////////////////////////////
  
  // I believe I did this by manipulating the camera view instead of modifying the object
  
  // trajectory -> angle
  const angle = rotFlag ? curRotAngle : Math.atan2(dY, dX) * 180 / Math.PI;
  //console.log({curPosX, curPosX, angle });
  mvMatrix
    .translate(curPosX, curPosY, 0)
    .rotate(angle, 0, 0, 1);
  
  // draw the object
  gl.uniformMatrix4fv(u_ModelMatrix, false, mvMatrix.elements);
  gl.uniform4f(u_FragColor, 1,0,0,1);
  gl.drawArrays(gl.TRIANGLES, 0, 3);   // draw the triangle
  gl.uniform4f(u_FragColor, 1,1,1,1);
  gl.drawArrays(gl.POINTS, 0, 4);      // draw the CM


  mvPopMatrix();

  gl.uniformMatrix4fv(u_ModelMatrix, false, mvMatrix.elements);


  // draw the path as lines
  if(renderMode>0){
    gl.uniform4f(u_FragColor, 1,1,0,1);
    gl.bufferData(gl.ARRAY_BUFFER, new Float32Array(past), gl.DYNAMIC_DRAW); // copy the vertices

    var len = past.length/2;
    gl.drawArrays(gl.LINE_STRIP, 0, len); 
  }


}




function animate()
{
  // Calculate the elapsed time
  var now = Date.now();
  var elapsed = now - lastAnimationTime;
  if(elapsed < 1000/fps) return;

  // record the current time
  lastAnimationTime = now;

  // compute new angle
  curRotAngle += angSpeed;
  if (curRotAngle >360) curRotAngle-=360;

  // compute new position
  curPosX += speed * dX; 
  curPosY += speed * dY; 

  const newTrajectory = Math.random() + 0.5;

  // random number
  if (curPosX < -boardW/2.0){ // left intersection
    curPosX = -boardW/2.0;
    dX *= -newTrajectory;
  }

  if (curPosX> boardW/2.0){ // right intersection
    curPosX = boardW/2.0;
    dX *= -newTrajectory;
  }

  if (curPosY < -boardH/2.0){ // bottom intersection
    curPosY = -boardH/2.0;
    dY *= -newTrajectory;
  }

  if (curPosY > boardH/2.0){ // top intersection
    curPosY = boardH/2.0;
    dY *= -newTrajectory;
  }

  past.push(curPosX);
  past.push(curPosY);

}


function tick()
{
  if (!pauseFlag) animate();                                   // update position and rotation angle
  drawScene(gl,u_ModelMatrix,u_FragColor,n);   // draw the object
  requestAnimationFrame(tick, canvas);         // request a new animation frame
}


function main() 
{
  // Retrieve <canvas> element
  canvas = document.getElementById('webgl');

  // Get the rendering context for WebGL
  gl = getWebGLContext(canvas);
  if (!gl) {
    console.log('Failed to get the rendering context for WebGL');
    return;
  }

  // get canvas height/width
  gl.viewportWidth = canvas.width;
  gl.viewportHeight = canvas.height;
  console.log(gl.viewportWidth);
  console.log(gl.viewportHeight);

  // Initialize shaders
  if (!initShaders(gl, VSHADER_SOURCE, FSHADER_SOURCE)) {
    console.log('Failed to intialize shaders.');
    return;
  }

  // Write the positions of vertices to a vertex shader
  n = initVertexBuffers(gl);
  if (n < 0) {
    console.log('Failed to set the positions of the vertices');
    return;
  }

  // get pointers to shader uniform variables 
  u_ModelMatrix = gl.getUniformLocation(gl.program, 'u_ModelMatrix');
  if (!u_ModelMatrix) {
    console.log('Failed to get the storage location of u_ModelMatrix');
    return;
  }

  u_FragColor = gl.getUniformLocation(gl.program, 'u_FragColor');
  if (!u_FragColor) {
    console.log('Failed to get the storage location of u_FragColor');
    return;
  }


  // set button listeners
  var speedUpBtn = document.getElementById('speedUpButton');
  speedUpBtn.addEventListener('click', speedUp);

  var speedDownBtn = document.getElementById('speedDownButton');
  speedDownBtn.addEventListener('click', speedDown);

  var zoomInBtn = document.getElementById('zoomInButton');
  zoomInBtn.addEventListener('click', zoomIn);

  var zoomOutBtn = document.getElementById('zoomOutButton');
  zoomOutBtn.addEventListener('click', zoomOut);

  var renderModeBtn = document.getElementById('renderModeButton');
  renderModeBtn.addEventListener('click', toggleRenderMode);

  var pauseBtn = document.getElementById('pauseButton');
  pauseBtn.addEventListener('click', togglePause);

  const rotButton = document.getElementById("rotateButton");
  rotButton.addEventListener("click", toggleRotate);

  // set path angle
  var pathBaseAngle = 30;
  dX = Math.cos(Math.PI * pathBaseAngle / 180.0);
  dY = Math.sin(Math.PI * pathBaseAngle / 180.0);


  // draw
  initScene(gl,u_ModelMatrix,u_FragColor,n);
  //  drawScene(gl,u_ModelMatrix,u_FragColor,n);
  tick();
}





