package billiard;

import java.io.File;
import java.io.IOException;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

public class obj2D {
    
    private float posX;
    private float posY;
    private float sizeX;
    private float sizeY;
    private Texture tex;
    
    //Возвращает путь к директории проекта.
    private String getRootDir(){
        
        return new File(".").getAbsoluteFile().getParentFile().getAbsolutePath();
    }
    
    /**
     * Создать объект и задать ему основные параметры.
     * 
     * @param positionX
     * @param positionY
     * @param sizeXf
     * @param sizeYf
     */
    public void createObjectQuad(float positionX, float positionY, float sizeXf, float sizeYf){
        
        posX = positionX;
        posY = positionY;
        sizeX = sizeXf;
        sizeY = sizeYf;
    }
            
    /**
     * Отрисовать объект.
     * 
     * @param isTextured
     */
    public void drawObjectQuad(boolean isTextured){
        
        if(isTextured){
            GL11.glEnable(GL11.GL_TEXTURE_2D);
            tex.bind();
        }
        
        GL11.glEnable(GL11.GL_QUADS);
        GL11.glBegin(GL11.GL_QUADS);
        
        GL11.glVertex2f(posX, posY+sizeY);          GL11.glTexCoord2f(1, 0);
        GL11.glVertex2f(posX+sizeX, posY+sizeY);    GL11.glTexCoord2f(1, 1); 
        GL11.glVertex2f(posX+sizeX, posY);          GL11.glTexCoord2f(0, 1);
        GL11.glVertex2f(posX, posY);                GL11.glTexCoord2f(0, 0);
        
        GL11.glEnd();
        GL11.glDisable(GL11.GL_QUADS);
        if(isTextured) GL11.glDisable(GL11.GL_TEXTURE_2D);
        
    }
    
    /**
     * Привязать текстуру, используя путь к PNG файлу.
     * @param filename
     * @throws IOException
     */
    public void bindTextureFromFile(String filename) throws IOException{
        
        tex = TextureLoader.getTexture("PNG",ResourceLoader.getResourceAsStream(getRootDir()+"/res/"+filename));
    }

    
    /**
     * Вернуть истину, если курсор наведен на объект.
     * 
     * @return
     */
    public boolean isHover(){
        
        return (engine.getFloatX(Mouse.getX()) > posX &&
                engine.getFloatX(Mouse.getX()) < posX+sizeX &&
                engine.getFloatY(Mouse.getY()) > posY &&
                engine.getFloatY(Mouse.getY()) < posY+sizeY);
    }
    
    /**
     * Наложение цвета на объект.
     * 
     * @param isTextured
     */
    public void fillColor(boolean isTextured){
        
        GL11.glColor3f(0.5f, 0.5f, 0);
        drawObjectQuad(isTextured);
        GL11.glColor3f(1, 1, 1);
    }
}
