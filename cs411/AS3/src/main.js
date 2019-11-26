

const options = {
    zoom: 1,
    translateFirst: false,
    triangles: true,
    bgr: false,
};

const globals = {
    // canvas element
    // gl webgl context
    // 
};

const buttonActions = {
    zoomIn: () => options.zoom *= 1.1,
    zoomOut: () => options.zoom *= 0.9,
    translateFirst: () => options.translateFirst = true,
    rotateFirst: () => options.translateFirst = false,
};

const VSHADER_SOURCE = `
attribute vec4 a_Position;
uniform mat4 u_ModelMatrix;
attribute vec3 a_Color;
varying vec3 v_Color;

void main() {
    gl_Position = u_ModelMatrix * a_Position;
    gl_Position.x *= 0.5;
    gl_PointSize = 10.0;
    v_Color = a_Color;
}
`;

const FSHADER_SOURCE = `
precision mediump float;
varying vec3 v_Color;
uniform int u_Bgr;
void main() {
    if (u_Bgr == 0)
        gl_FragColor = vec4(v_Color, 1);
    else
        gl_FragColor = vec4(v_Color.bgr, 1);
}
`;

let canvas;
let gl;
let u_Bgr;
let mvMatrix = new Matrix4();

let buffer;

function initVertexBuffers() {
    // something like a fancy compass rose
    // idk why i wasted all my time on this lol
    let makeShape = () => {
        
        let ret = [ 0, 0, 1, 1, 1 ]; // x,y,r,g,b
        
        const d_s = 0.5, d_l = Math.random() / 4 + 0.5;
        let xy = false;
        for (let i = 0; i < 4; i++) {
            let signX = 1, signY = 1;
            if (i >= 1 && i < 3)
                signX = -1;
            if (i >= 2)
                signY = -1;
            console.log({signX, signY});
            // x,y,r,g,b
            xy = !xy;

            ret = ret.concat([
                xy? 1 * signX : 0, xy ? 0 : signY * 1, Math.random(), Math.random(), Math.random(),
                (signX == signY ? Math.cos : Math.sin)(Math.PI * 2/16) * d_s * signX, (signX == signY ? Math.sin : Math.cos)(Math.PI * 2/16) * d_s * signY, Math.random(), Math.random(), Math.random(),
                d_l * signX, d_l * signY, Math.random(), Math.random(), Math.random(),
                (signX == signY ? Math.sin : Math.cos)(Math.PI * 2/16) * d_s * signX, (signX == signY ? Math.cos : Math.sin)(Math.PI * 2/16) * d_s * signY, Math.random(), Math.random(), Math.random(),
            ]);
        }
        ret = ret.concat([ 1, 0, Math.random(), Math.random(), Math.random() ]);
        // console.log({ret})
        return new Float32Array(ret);
    };

    buffer = makeShape();

    // make buffer obj
    let vBuffer = gl.createBuffer();
    if (!vBuffer) {
        console.error("Failed to create buffer object");
        return false;
    }

    // bind buffer to target
    gl.bindBuffer(gl.ARRAY_BUFFER, vBuffer);
    // write to buffer
    gl.bufferData(gl.ARRAY_BUFFER, buffer, gl.DYNAMIC_DRAW);

    let a_Position = gl.getAttribLocation(gl.program, "a_Position");
    if (a_Position < 0) {
        console.error("Failed to get the storage location of a_Position");
        return -1;
    }

    let a_Color = gl.getAttribLocation(gl.program, "a_Color");
    if (a_Color < 0) {
        console.error("Failed to get storage location of a_Color");
        return -1;
    }
    // Assign buffer obj to a_Position variable
    gl.vertexAttribPointer(a_Position, 2, gl.FLOAT, false, 5 * buffer.BYTES_PER_ELEMENT, 0);
    // Enable assignment to a_Position variable
    gl.enableVertexAttribArray(a_Position);
    
    gl.vertexAttribPointer(a_Color, 3, gl.FLOAT, false, 5 * buffer.BYTES_PER_ELEMENT, 2 * buffer.BYTES_PER_ELEMENT);
    gl.enableVertexAttribArray(a_Color);

}

function initScene() {

    gl.viewport(0,0, gl.viewportWidth, gl.viewportHeight);
    mvMatrix.setIdentity();

    let pMatrix = new Matrix4();
    pMatrix.setIdentity();

    mvMatrix.multiply(pMatrix);
}

function drawScene() {
    // reset
    gl.clearColor(0,0,0,1);
    gl.clear(gl.COLOR_BUFFER_BIT);

    mvMatrix.setScale(options.zoom, options.zoom, options.zoom);
    if (options.translateFirst) {
        mvMatrix.translate(0.1, 0.1, 0);
        mvMatrix.rotate(15, 0, 0, 1);
    } else {
        mvMatrix.rotate(15, 0, 0, 1 );
        mvMatrix.translate(0.2, 0.5, 0);
    }
    console.log({mvMatrix});
    // draw triangles
    gl.uniformMatrix4fv(u_ModelMatrix, false, mvMatrix.elements);
    if (options.triangles)
        gl.drawArrays(gl.TRIANGLE_FAN, 0, 18);
    else
        gl.drawArrays(gl.POINTS, 0, 18);

}

function main() {
    canvas = document.getElementById("webgl");
    gl = getWebGLContext(canvas, false);
    if (!gl) {
        console.error("Failed to get rendering context for WebGL");
        return;
    }
    gl.viewportWidth = canvas.width;
    gl.viewportHeight = canvas.height;
    
    console.log({
        "gl_vp_width" : gl.viewportWidth,
        "gl_vp_height" : gl.viewportHeight,
    });

    if (!initShaders(gl, VSHADER_SOURCE, FSHADER_SOURCE)) {
        console.error("Failed to initialize shaders");
        return;
    }

    n = initVertexBuffers(gl);
    if (n < 0) {
        console.error("Failed to set positions of verticies");
        return;
    }

    // uniform variable ptrs
    u_ModelMatrix = gl.getUniformLocation(gl.program, "u_ModelMatrix");
    if (!u_ModelMatrix) {
        console.error("Failed to get storage location of u_")
    }

    u_Bgr = gl.getUniformLocation(gl.program, 'u_Bgr');
    if (!u_Bgr) {
        console.error("Failed to get the storage location of u_Bgr");
        return;
    }


    // add button events
    document.getElementById("rotateFirstButton")
        .addEventListener("click", () => {
            options.translateFirst = false;
            drawScene();
        });    
    document.getElementById("translateFirstButton")
        .addEventListener("click", () => {
            options.translateFirst = true;
            drawScene();
        });
    document.getElementById("trianglesButton")
        .addEventListener("click", () => {
            options.triangles = !options.triangles;
            drawScene();
        });
    document.getElementById("BGR-Button")
        .addEventListener("click", () => {
            options.bgr = !options.bgr;
            gl.uniform1i(u_Bgr, options.bgr ? 1 : 0);
            drawScene();
        });
    document.getElementById("zoomInButton")
        .addEventListener("click", () => {
            options.zoom += .1;
            drawScene();
        });
    document.getElementById("zoomOutButton")
        .addEventListener("click", () => {
            options.zoom -= .1;
            drawScene();
        });

    drawScene();
}
