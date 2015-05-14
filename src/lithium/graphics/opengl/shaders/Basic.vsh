#version 330

layout(location = 0) in vec3 vertex_in;
layout(location = 1) in vec2 uv_in;
layout(location = 2) in vec3 normal_in;

uniform mat4 matProj, matView, matModel;

void main() {
    gl_Position = matProj * matView * matModel * vec4(vertex_in, 1);
}
