uniform extern texture ScreenTexture;    

sampler ScreenS = sampler_state
{
    Texture = <ScreenTexture>;    
};

struct PS_INPUT
{
    float2 tex0 : TEXCOORD0;
};

float2 screenSize;
float2 centerCoord;
float  distanceFromCam;

float4 BlackHolePS( in PS_INPUT IN ) : COLOR0
{
	float4 color;
	
	float2 balanced = IN.tex0 - centerCoord;
	float2 normalized = normalize(balanced);
	float distance = length(balanced);
	
	float2 pos = IN.tex0.xy;
	pos.x = pos.x * screenSize.x;
	pos.y = pos.y * screenSize.y;
	
	// The strength of the gravitational field at this point:
	float scaled = distance * distanceFromCam;
	float strength = 1 / ( scaled * scaled );
	float3 rayDirection = float3(0, 0, 1);
	float3 surfaceNormal = normalize( float3(normalized, 1.0 / strength) );
	float3 newBeam = refract(rayDirection, surfaceNormal, 2.6);
	float2 newPos = pos + float2(newBeam.x, newBeam.y) * 300;
	color = tex2D(ScreenS, (newPos) / screenSize); 
	color *= length(newBeam);
	color.a = 1.0f;
	
	return color;
}

technique
{
    pass P0
    {
        PixelShader = compile ps_2_0 BlackHolePS();
    }
}
