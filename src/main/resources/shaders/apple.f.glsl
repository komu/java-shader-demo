// Adapted from code by Iñigo Quílez (http://www.iquilezles.org/blog/?p=1656)
#version 120

uniform float time;
varying vec2 textureCoordinate;

float hash(float n)
{
    return fract(sin(n)*43758.5453);
}

float noise(in vec3 x)
{
    vec3 p = floor(x);
    vec3 k = fract(x);
    k = k*k*(3.0-2.0*k);

    float n = p.x + p.y*57.0 + p.z * 113.0;

    float a = hash(n);
    float b = hash(n+1.0);
    float c = hash(n+57.0);
    float d = hash(n+58.0);

    float e = hash(n+113.0);
    float f = hash(n+114.0);
    float g = hash(n+170.0);
    float h = hash(n+171.0);

    float res = mix(mix(mix(a, b, k.x), mix(c, d, k.x), k.y),
                    mix(mix(e, f, k.x), mix(g, h, k.x), k.y),
                    k.z);
    return res;
}

float fbm(in vec3 p)
{
    float f = 0.0;
    f += 0.5000*noise(p); p*=2.02;
    f += 0.2500*noise(p); p*=2.03;
    f += 0.1250*noise(p); p*=2.01;
    f += 0.0625*noise(p);
    return f/0.9375;
}

vec3 floorMaterial(in vec3 pos, in vec3 nor)
{
    vec3 col = vec3(0.6, 0.5, 0.3);

    float f = fbm(pos*vec3(6.0, 0.0, 0.5));
    col = mix(col, vec3(0.3, 0.2, 0.1), f);

    f = smoothstep(0.6, 1.0, fbm(pos*48.0));
    col = mix(col, vec3(0.2, 0.2, 0.15), f);

    float ao = 0.2 + 0.8*smoothstep(0.4, 2.0, length(pos.xz));
    col *= ao;
    return col;
}

vec3 fruitMaterial(in vec3 pos, in vec3 nor)
{
    float a = atan(pos.x, pos.z);
    float r = length(pos.xz);

    // red
    vec3 col = vec3(1.0, 0.0, 0.0);

    // mix to green
    float f = smoothstep(0.2, 1.0, fbm(pos));
    col = mix(col, vec3(0.8, 1.0, 0.2), f);

    // make it dirty
    f = smoothstep(0.0, 1.0, fbm(pos*4.0));
    col *= 0.8 + 0.2*f;

    // freckles
    f = smoothstep(0.7, 0.9, fbm(pos*48.0));
    col = mix(col, vec3(0.9, 0.9, 0.6), f);

    // stripes
    f = fbm(vec3(a*7.0, pos.y, pos.z)*2.0);
    f *= smoothstep(0.4, 1.2, pos.y+0.5*(noise(pos.yxz)-0.5));
    col = mix(col, vec3(0.4, 0.2, 0.0), 0.5*f);

    // top cap
    f = smoothstep(0.1, 0.2, r);
    col = mix(col, vec3(0.6, 0.6, 0.5), 1.0-f);

    // fake ao
    float ao = 0.5 + 0.5*nor.y;
    col *= ao;

    return col;
}

float fruitShape(in vec3 p)
{
    float f = pow(dot(p.xz, p.xz), 0.2);
    p.y -= 0.5*f;
    return length(p) - 1.0;
}

float floorShape(in vec3 p)
{
    return p.y + 0.25;
}

vec2 map(in vec3 p)
{
    vec2 d1 = vec2(fruitShape(p), 1.0);
    vec2 d2 = vec2(floorShape(p), 2.0);

    if (d2.x<d1.x) d1 = d2;
    return d1;
}

vec3 calcNormal(in vec3 p)
{
    vec3 e = vec3(0.001, 0.0, 0.0);
    vec3 n;
    n.x = map(p+e.xyy).x - map(p-e.xyy).x;
    n.y = map(p+e.yxy).x - map(p-e.yxy).x;
    n.z = map(p+e.yyx).x - map(p-e.yyx).x;
    return normalize(n);
}

float softShadow(in vec3 ro, in vec3 rd)
{
    float res = 1.0;

    for (float t = 0.1; t < 8.0; )
    {
        float h = map(ro+t*rd).x;
        if (h < 0.0001) return 0.0;

        res = min(res, 8.0*h/t);
        t += h;
    }

    return res;
}

vec2 intersect(in vec3 ro, in vec3 rd)
{
    for (float t = 0.0; t < 6.0; )
    {
        vec2 h = map(ro+t*rd);
        if (h.x < 0.0001) return vec2(t, h.y);
        t += h.x;
    }
    return vec2(0.0);
}

void main()
{
    vec2 q = textureCoordinate;

    vec2 p = (-1.0 + 2.0*q)*vec2(1.77, 1.0);
    vec3 ro = 1.8*vec3(cos(0.2*time), 1.0, sin(0.2*time));
    vec3 ww = normalize(vec3(0.0, 0.0, 0.0) - ro);
    vec3 uu = normalize(cross(vec3(0.0, 1.0, 0.0), ww));
    vec3 vv = normalize(cross(ww,uu));
    vec3 rd = normalize(p.x*uu + p.y*vv + 1.5*ww);

    vec2 t = intersect(ro, rd);
    vec3 col = vec3(0.8);
    if (t.y > 0.5)
    {
        vec3 pos = ro + t.x*rd;
        vec3 nor = calcNormal(pos);
        vec3 lig = normalize(vec3(1.0, 0.8, 0.6));
        vec3 blig = vec3(-lig.x, lig.y, -lig.z);
        vec3 ref = reflect(rd, nor);

        float con = 1.0;
        float amb = 0.5 + 0.5*nor.x;
        float dif = max(0.0, dot(nor, lig));
        float bac = max(0.0, 0.2+0.8*dot(nor, blig));
        float sha = softShadow(pos, lig);
        float spe = pow(clamp(dot(lig, ref), 0.0, 1.0), 8.0);
        float rim = pow(1.0+dot(nor, rd), 2.5);

        col  = con*vec3(0.1, 0.15, 0.2);
        col += amb*vec3(0.1, 0.15, 0.2);
        col += dif*vec3(1.0, 0.97, 0.85)*sha;
        col += bac*vec3(1.0, 0.97, 0.85);

        if (t.y > 1.5)
        {
            col *= floorMaterial(pos, nor);
        }
        else
        {
            col *= fruitMaterial(pos, nor);
        }

        col += 0.6*rim*amb;
        col += 0.6*spe*sha*amb;

        col = col*0.1 + 0.9*sqrt(col);
        col *= vec3(0.9, 0.8, 0.7);
    }

    col *= 0.2 + 0.8*pow(16.0*q.x*q.y*(1.0-q.x)*(1.0-q.y), 0.2);

    gl_FragColor = vec4(col, 1.0);
}
