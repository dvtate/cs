//
// Add matrix and vector functions to cuon-matrix
//

Matrix4.prototype.str = function () 
{
  var a = this.elements;
  return '['  + a[0] + ', ' + a[4] + ', ' + a[8] + ', '  + a[12] + '\n' +
         '  ' + a[1] + ', ' + a[5] + ', ' + a[9] + ', '  + a[13] + '\n' +
         '  ' + a[2] + ', ' + a[6] + ', ' + a[10] + ', ' + a[14] + '\n' + 
         '  ' + a[3] + ', ' + a[7] + ', ' + a[11] + ', ' + a[15] + ']';
}


Vector3.prototype.str = function () 
{
  var a = this.elements;
  return '['  + a[0] + ', ' + a[1] + ', ' + a[2] + ']';
}


Vector4.prototype.str = function () 
{
  var a = this.elements;
  return '['  + a[0] + ', ' + a[1] + ', ' + a[2] + ', ' + a[3] + ']';
}


Vector3.prototype.cross = function(v) 
{
    var a = this.elements;
    var b = v.elements;
    var ax = a[0], ay = a[1], az = a[2];
    var bx = b[0], by = b[1], bz = b[2];
    var out = new Vector3();

    out.elements[0] = ay * bz - az * by;
    out.elements[1] = az * bx - ax * bz;
    out.elements[2] = ax * by - ay * bx;
    return out;
}


//
// Add matrix stack
//

var pMatrix = new Matrix4();
var mvMatrix = new Matrix4();
var mvMatrixStack = [];


function mvPushMatrix() 
{
  var copy = new Matrix4();
  copy.set(mvMatrix);
  mvMatrixStack.push(copy);
}



function mvPopMatrix() 
{
   if (mvMatrixStack.length == 0) {
     throw "Invalid popMatrix!";
   }
  mvMatrix = mvMatrixStack.pop();
}





//function setMatrixUniforms() 
//{
//   gl.uniformMatrix4fv(shaderProgram.pMatrixUniform, false, pMatrix);
//   gl.uniformMatrix4fv(shaderProgram.mvMatrixUniform, false, mvMatrix);
//}









