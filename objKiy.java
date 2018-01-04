package billiard;

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.opengl.Texture;

public class objKiy {
    
    private float translatef;
    private Texture tex;
    private float angle;
    private billiardsBall white;
    
    /**
     * Создать объект с необходимыми параметрами.
     * 
     * @param texture
     * @param ang
     * @param trf
     * @param ball
     */
    public void create(Texture texture, float ang, float trf, billiardsBall ball){
        
        tex = texture;
        angle = ang;
        translatef = trf;
        white = ball;
    }
    
    /**
     * Уничтожить объект для экономии памяти.
     */
    public void destroy(){
        
        tex = null;
        white = null;
        angle = 0;
        translatef = 0;
    }
    
    /**
     * Отрисовать объект.
     */
    public void draw(){
        
        GL11.glPushMatrix(); 
        GL11.glLoadIdentity();

        // "Привязываем" кий к белому шару и вращаем его под градусом, который получили через метод getAngleOfMousePos.
        GL11.glTranslatef(white.getPosX()+0.02f, white.getPosY()+0.03f, 0f);
        GL11.glRotatef (angle, 0, 0, 1); 
        GL11.glTranslatef(translatef, white.getPosY()*-1, 0f);

        GL11.glEnable(GL11.GL_QUADS);
        GL11.glEnable(GL11.GL_TEXTURE_2D);

        tex.bind();
        GL11.glBegin(GL11.GL_QUADS);

        GL11.glVertex2f(white.getPosX(), white.getPosY()+0.4f);               GL11.glTexCoord2f(1, 0);
        GL11.glVertex2f(white.getPosX()-0.8f, white.getPosY()+0.4f);          GL11.glTexCoord2f(1, 1); 
        GL11.glVertex2f(white.getPosX()-0.8f, white.getPosY()-0.4f);          GL11.glTexCoord2f(0, 1);
        GL11.glVertex2f(white.getPosX(), white.getPosY()-0.4f);               GL11.glTexCoord2f(0, 0);

        GL11.glEnd();
        GL11.glDisable(GL11.GL_QUADS);
        GL11.glDisable(GL11.GL_TEXTURE_2D);

        GL11.glPopMatrix();
    }
    
    /**
     * Получить точку центра объекта по оси Х
     * 
     * @return
     */
    public float getCenterX(){
        
        return white.getPosX()-0.4f;
    }
    
    /**
     * Получить точку центра объекта по оси Y
     * 
     * @return
     */
    public float getCenterY(){
        
        return white.getPosY();
    }
    
}
