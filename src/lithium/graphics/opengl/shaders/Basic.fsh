#version 330

layout(location = 0) out vec4 color;

in vec2 uv_tan;

void main() {
    color = vec4(uv_tan, 0, 1);
}
