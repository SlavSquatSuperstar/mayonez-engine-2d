// Simple fragment shader with whitespace

#version 330 core

in vec4 fColor;
in vec2 fTexCoords;

// Parse uniform
  uniform   sampler2D
 uTexture ;  uniform
  bool uUseTexture ;

out vec4 color;

void main()
{
    color = fColor * texture(uTexture, fTexCoords);
}
