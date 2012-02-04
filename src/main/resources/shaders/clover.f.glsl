// Adapted from code by Iñigo Quílez (http://www.iquilezles.org/blog/?p=1613)
#version 120

uniform float time;

vec3 computeColor(vec2 p)
{
    p -= vec2(0.7, 0.8);

    float r = sqrt(dot(p, p));
    float a = atan(p.y, p.x) + time*0.1;
    float s = 0.5 + 0.5*sin(3.0*a);
    float t = 0.15 + 0.35*pow(s, 0.3);
    t += 0.1*pow(0.5+0.5*cos(6.0*a), 0.5);
    float h = r/t;
    float f = 0.0;
    if (h < 1.0) f = 1.0;

    return mix(vec3(1.0), vec3(0.5*h, 0.5+0.5*h, 0.0), f);
}

void main()
{
    vec2 p = 1.4*gl_FragCoord.xy/vec2(1280,800);

    gl_FragColor = vec4(computeColor(p), 1);
}
