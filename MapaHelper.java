package utiles;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

public class MapaHelper {

    public static Array<Rectangle> cargarRectangulos(TiledMap mapa, String nombreCapa) {
        Array<Rectangle> lista = new Array<>();

        if (mapa.getLayers().get(nombreCapa) != null) {
            MapObjects objetos = mapa.getLayers().get(nombreCapa).getObjects();
            System.out.println("Objetos en capa '" + nombreCapa + "': " + objetos.getCount());

            for (MapObject obj : objetos) {
                System.out.println("Objeto: " + obj.getName() + ", clase: " + obj.getClass().getSimpleName());

                if (obj instanceof RectangleMapObject) {
                    lista.add(((RectangleMapObject) obj).getRectangle());
                }
            }
        } else {
            System.out.println("⚠ La capa '" + nombreCapa + "' no existe en el mapa.");
        }

        return lista;
    }


}

