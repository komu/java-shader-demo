#version 120

attribute vec2 vertexTextureCoordinate;
varying vec2 textureCoordinate;

void main()
{
    gl_Position = gl_ModelViewProjectionMatrix*gl_Vertex;
    textureCoordinate = vertexTextureCoordinate;
}
