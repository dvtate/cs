"use strict";

/////////////////////////////////////////////////////////////////////////////////////////
//
// cs411 assignment 1 - raster graphics
//
/////////////////////////////////////////////////////////////////////////////////////////


var ctx;
var imageData;

var pauseFlag=1;
var lineFlag=1;
var triangleFlag=1;
var fillFlag=1;


function togglePause() {
  pauseFlag= 1 - pauseFlag;
  console.log('pauseFlag = %d', pauseFlag);
}

function toggleLine() {
  lineFlag= 1 - lineFlag;
  console.log('lineFlag = %d', lineFlag);
}

function toggleTriangle() {
  triangleFlag= 1 - triangleFlag;
  console.log('triangleFlag = %d', triangleFlag);
}

function toggleFill() {
  fillFlag= 1 - fillFlag;
  console.log('fillFlag = %d', fillFlag);
}


function animate()
{
  if(!pauseFlag) {
    if (lineFlag)
        drawRandomLineSegment();
    if (triangleFlag)
        drawRandomTriangle();
  }
  setTimeout(animate, 100); // call animate() in 100 msec
}


function initImage(img)
{
  var canvas = document.getElementById('mycanvas');
  ctx = canvas.getContext('2d');

  ctx.drawImage(img, 0, 0);
  imageData = ctx.getImageData(0,0,canvas.width, canvas.height); // get reference to image data
}


function main()
{
  // load and display image
  var img = new Image();
  img.src = '../data/frac2.png';
  img.onload = function() { initImage(this);}

  // set button listeners
  var grayscalebtn = document.getElementById('grayscaleButton');
  grayscalebtn.addEventListener('click', grayscale);

  var pausebtn = document.getElementById('pauseButton');
  pausebtn.addEventListener('click', togglePause);

  var linebtn = document.getElementById('lineButton');
  linebtn.addEventListener('click', toggleLine);

  var trianglebtn = document.getElementById('triangleButton');
  trianglebtn.addEventListener('click', toggleTriangle);

  var fillbtn = document.getElementById('fillButton');
  fillbtn.addEventListener('click', toggleFill);

  // start animation
  animate();
}


/////////////////////////////////////////////////////////////////////////////////////////
//
// conversion to grayscale
//
/////////////////////////////////////////////////////////////////////////////////////////

function grayscale()
{
  var data = imageData.data;
  for (var i = 0; i < data.length; i += 4) {
    var m = (data[i] + data[i +1] + data[i +2]) / 3;
    data[i]     = m; // red
    data[i + 1] = m; // green
    data[i + 2] = m; // blue
  }
  ctx.putImageData(imageData, 0, 0);
}


/////////////////////////////////////////////////////////////////////////////////////////
//
// draw lines
//
/////////////////////////////////////////////////////////////////////////////////////////


//<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
// REPLACE THIS WITH YOUR FUNCTION FOLLOWING THE ASSIGNMENT SPECIFICATIONS
//<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
function drawLineSegment(vs,ve,color)
{

  var data = imageData.data;
  var h = imageData.height;
  var w = imageData.width;

  //
  function fill(p) {
    data[(p[1]*w + p[0]) * 4 + 0] = color[0];
    data[(p[1]*w + p[0]) * 4 + 1] = color[1];
    data[(p[1]*w + p[0]) * 4 + 2] = color[2];
  }

  (function lineBres(a, b) {
    [ a , b ] = [ a.map(Math.floor), b.map(Math.floor) ];
  	const dx = Math.abs(a[0] - b[0]), dy = Math.abs(a[1] - b[1]);
  	let p = 2 * dy - dx;
  	const twoDy = 2 * dy, twoDyDx = 2 * (dy - dx);
  	let coord, xEnd;

  	if (a[0] > b[0]) {
  		coord = b;
  		xEnd = a[0];
  	} else {
  		coord = a;
  		xEnd = b[0];
  	}

  	fill(coord);

  	while (coord[0] < xEnd) {
  		coord[0]++;
  		if (p < 0) {
  			p += twoDy;
  		} else {
  			coord[1]++;
  			p += twoDyDx;
  		}
      	fill(coord);
  	}
  })(vs, ve);


  // update image
  ctx.putImageData(imageData, 0, 0);
}


function drawRandomLineSegment()
{
  var h = imageData.height;
  var w = imageData.width;

  var xs=Math.floor(Math.random()*w);
  var ys=Math.floor(Math.random()*h);
  var xe=Math.floor(Math.random()*w);
  var ye=Math.floor(Math.random()*h);
  var r=Math.floor(Math.random()*255);
  var g=Math.floor(Math.random()*255);
  var b=Math.floor(Math.random()*255);

  drawLineSegment([xs,ys] ,[xe,ye],[r,g,b]);
}


/////////////////////////////////////////////////////////////////////////////////////////
//
// draw triangles
//
/////////////////////////////////////////////////////////////////////////////////////////


function triangleArea(a,b,c)
{
  var area = ((b[1] - c[1]) * (a[0] - c[0]) + (c[0] - b[0]) * (a[1] - c[1]));
  area = Math.abs(0.5*area);
  return area;
}

function vertexInside(v,v0,v1,v2)
{
  var T = triangleArea(v0,v1,v2);

  var alpha = triangleArea(v,v0,v1) /T ;
  var beta  = triangleArea(v,v1,v2) /T ;
  var gamma = triangleArea(v,v2,v0) /T ;

  if ((alpha>=0) && (beta>=0) && (gamma>=0) && (Math.abs(alpha+beta+gamma -1)<0.00001)) return true;
  else return false;
}


//<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
// REPLACE THIS WITH YOUR FUNCTION FOLLOWING THE ASSIGNMENT SPECIFICATIONS
//<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
function drawTriangle(v1,v2,v3,color){

    const randRgb = () => [
        Math.floor(Math.random() * 255),
        Math.floor(Math.random() * 255),
        Math.floor(Math.random() * 255),
    ];

    function fillBottomFlatTriangle(v1, v2, v3) {
        const invslope1 = (v2[0] - v1[0]) / (v2[1] - v1[1]),
              invslope2 = (v3[0] - v1[0]) / (v3[1] - v1[1]);

        let curx1 = v1[0], curx2 = v1[0];

        for (let scan = v1[1]; scan <= v2[1]; scan++) {
            drawLineSegment([ curx1, scan ], [ curx2, scan ],
                            fillFlag ? randRgb() : color);
            curx1 += invslope1;
            curx2 += invslope2;
        }
    }

    function fillTopFlatTriangle(v1, v2, v3) {
        const invslope1 = (v3[0] - v1[0]) / (v3[1] - v1[1]),
              invslope2 = (v3[0] - v2[0]) / (v3[1] - v2[1]);

        let curx1 = v3[0], curx2 = v3[0];

        for (let scan = v3[1]; scan > v1[1]; scan--) {
            drawLineSegment([ curx1, scan ], [ curx2, scan ],
                            fillFlag ? randRgb() : color);
            curx1 -= invslope1;
            curx2 -= invslope2;
        }
    }

    // sort coords by y elevation
    [v1, v2, v3] = [v1, v2, v3].sort((a,b) => a[1] - b[1]);

    // trivial cases
    if (v2[1]  == v3[1])
        return fillBottomFlatTriangle(v1, v2, v3);
    if (v1[1] == v2[1])
        return fillTopFlatTriangle(v1, v2, v3);

    // split triangle in half to form top and bottom flat triangles
    const v4 = [ v1[0] + ((v2[1] - v1[1]) / (v3[1] - v1[1])) * (v3[0] - v1[0]), v2[1] ];
    fillTopFlatTriangle(v2, v4, v3);
    fillBottomFlatTriangle(v1, v2, v4);

}


function drawRandomTriangle()
{
  var h = imageData.height;
  var w = imageData.width;

  var v0x=Math.floor(Math.random()*w);
  var v0y=Math.floor(Math.random()*h);
  var v1x=Math.floor(Math.random()*w);
  var v1y=Math.floor(Math.random()*h);
  var v2x=Math.floor(Math.random()*w);
  var v2y=Math.floor(Math.random()*h);
  var r=Math.floor(Math.random()*255);
  var g=Math.floor(Math.random()*255);
  var b=Math.floor(Math.random()*255);

  drawTriangle([v0x,v0y], [v1x,v1y], [v2x,v2y], [r,g,b]);

}
