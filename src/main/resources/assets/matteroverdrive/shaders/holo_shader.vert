attribute vec4 gl_Color;
attribute vec4 gl_MultiTex0;

varying vec4 gl_FrontColor;

void main()
{
    gl_Position = ftransform();
    gl_FrontColor = gl_Color;
    gl_Tex[0] = gl_MultiTex0;
}