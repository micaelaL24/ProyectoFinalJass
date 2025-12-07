package elementos;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public class Estrella {
    private Texture textura;
    private Rectangle hitbox;

    public Estrella(float x, float y, Texture textura) {
        this.textura = textura;
        this.hitbox = new Rectangle(x, y, textura.getWidth(), textura.getHeight());
    }

    public void dibujar(SpriteBatch batch) {
        batch.draw(textura, hitbox.x, hitbox.y);
    }

    public Rectangle getHitbox() {
        return hitbox;
    }
}
