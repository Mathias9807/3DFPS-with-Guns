package lithium.graphics.opengl;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.*;

import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.*;

public class FBO {
	
	private int 		frameBufferID, 
						depthBufferID, 
						depthTextureID;
	private int[] 		texturesID;
	private IntBuffer 	buffers;
	public int 			width, 
						height;
	
	public FBO(int w, int h) {
		width = w;
		height = h;
		frameBufferID = glGenFramebuffers();
	}

	public FBO withDepthTexture() {
		depthTextureID = glGenTextures();
		glBindFramebuffer(GL_FRAMEBUFFER, frameBufferID);
		
		glBindTexture(GL_TEXTURE_2D, depthTextureID);
		glTexImage2D(GL_TEXTURE_2D, 0, GL14.GL_DEPTH_COMPONENT32, width, height, 0, GL_DEPTH_COMPONENT, GL_FLOAT, (java.nio.ByteBuffer) null);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);
		glTexParameteri(GL_TEXTURE_2D, GL14.GL_TEXTURE_COMPARE_FUNC, GL_LEQUAL);
		glTexParameteri(GL_TEXTURE_2D, GL14.GL_TEXTURE_COMPARE_MODE, GL_COMPARE_REF_TO_TEXTURE);
		
		GL32.glFramebufferTexture(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, depthTextureID, 0);
		
		if (glCheckFramebufferStatus(GL_FRAMEBUFFER) != GL_FRAMEBUFFER_COMPLETE) System.err.println("Failed to create Framebuffer. "); 
		
		glBindFramebuffer(GL_FRAMEBUFFER, 0);
		return this;
	}

	public FBO withColor(int attachments) {
		glBindFramebuffer(GL_FRAMEBUFFER, frameBufferID);
		
		texturesID = new int[attachments];
		
		buffers = BufferUtils.createIntBuffer(attachments);
		
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		for (int i = 0; i < attachments; i++) {
			texturesID[i] = glGenTextures();
			
			glBindTexture(GL_TEXTURE_2D, texturesID[i]);
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);
			glTexParameteri(GL_TEXTURE_2D, GL14.GL_TEXTURE_COMPARE_MODE, GL_COMPARE_REF_TO_TEXTURE);
			glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA16F, width, height, 0, GL_RGBA, GL_FLOAT, (java.nio.ByteBuffer) null);
			GL30.glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0 + i, GL_TEXTURE_2D, texturesID[i], 0);
			buffers.put(GL_COLOR_ATTACHMENT0 + i);
		}
//		GL20.glDrawBuffers(buffers);
		
		if (glCheckFramebufferStatus(GL_FRAMEBUFFER) != GL_FRAMEBUFFER_COMPLETE) System.err.println("Failed to create Framebuffer. "); 
		
		glBindFramebuffer(GL_FRAMEBUFFER, 0);
		return this;
	}
	
	public FBO withDepth() {
		depthBufferID = glGenRenderbuffers();
		glBindFramebuffer(GL_FRAMEBUFFER, frameBufferID);
		
		glBindRenderbuffer(GL_RENDERBUFFER, depthBufferID);
		glRenderbufferStorage(GL_RENDERBUFFER, GL14.GL_DEPTH_COMPONENT24, width, height);
		glFramebufferRenderbuffer(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, GL_RENDERBUFFER, depthBufferID);
		
		if (glCheckFramebufferStatus(GL_FRAMEBUFFER) != GL_FRAMEBUFFER_COMPLETE) System.err.println("Failed to create Framebuffer. "); 
		
		glBindFramebuffer(GL_FRAMEBUFFER, 0);
		return this;
	}
	
	public static void setFrameBuffer(FBO fbo) {
		if (fbo == null) {
			glBindFramebuffer(GL_FRAMEBUFFER, 0);
			glDrawBuffer(GL_FRONT);
			glViewport(0, 0, Display.getWidth(), Display.getHeight());
		}else {
			fbo.bindFrameBuffer();
		}
	}
	
	public void bindFrameBuffer() {
		glBindFramebuffer(GL_FRAMEBUFFER, frameBufferID);
		glDrawBuffer(GL_COLOR_ATTACHMENT0);
		glViewport(0, 0, width, height);
	}
	
	public void deleteFrameBuffer() {
		glDeleteFramebuffers(frameBufferID);
		glDeleteRenderbuffers(depthBufferID);
		if (texturesID == null) return;
		for (int i = 0; i < texturesID.length; i++) {
			glDeleteTextures(texturesID[i]);
		}
	}
	
	public int nextPOT(int i) {
		int r = 2;
		while (r < i) 
			r *= 2;
		return r;
	}
	
	public int getFrameBufferId() {
		return frameBufferID;
	}
	
	public int[] getTexturesId() {
		return texturesID;
	}
	
	public int getDepthBufferId() {
		return depthBufferID;
	}
	
	public int getDepthTextureId() {
		return depthTextureID;
	}
	
	public IntBuffer getBuffers() {
		return buffers;
	}
	
}
