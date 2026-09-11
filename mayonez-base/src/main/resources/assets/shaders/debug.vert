/* Debug shape fragment shader */

#type fragment
#version 330 core

in vec4 fColor; // Color

out vec4 color; // Output to framebuffer

void main()
{
    color = fColor;
}