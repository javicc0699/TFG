
package com.example.helldiversbuildhub;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import android.widget.ImageView;

import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

/**
 * Pruebas unitarias para UploadFragment, usando solo JUnit.
 * Se testea el método actualizarIcono(int, String).
 */
public class UploadFragmentTest {

    private UploadFragment fragment;

    // Stub de ImageView para capturar llamadas a setImageResource y setImageDrawable
    private static class StubImageView extends androidx.appcompat.widget.AppCompatImageView {
        Integer lastResource = null;
        boolean drawableCleared = false;

        public StubImageView() {
            super(null);
        }

        @Override
        public void setImageResource(int resId) {
            lastResource = resId;
            drawableCleared = false;
        }

        @Override
        public void setImageDrawable(android.graphics.drawable.Drawable drawable) {
            if (drawable == null) {
                drawableCleared = true;
                lastResource = null;
            }
        }
    }

    @Before
    public void setUp() throws Exception {
        fragment = new UploadFragment();

        // Construir lista de Stratagems simulada
        Stratagem s1 = new Stratagem("Alpha Strike", 111);
        Stratagem s2 = new Stratagem("Reinforce", 222);
        Stratagem s3 = new Stratagem("Supply Pack", 333);
        List<Stratagem> lista = Arrays.asList(s1, s2, s3);

        // Asignar stratagemsList al fragment vía reflexión
        Field stratListField = UploadFragment.class.getDeclaredField("stratagemsList");
        stratListField.setAccessible(true);
        stratListField.set(fragment, lista);

        // Preparar iconosStrats con StubImageView
        Field iconosField = UploadFragment.class.getDeclaredField("iconosStrats");
        iconosField.setAccessible(true);
        StubImageView[] stubs = new StubImageView[4];
        for (int i = 0; i < 4; i++) {
            stubs[i] = new StubImageView();
        }
        iconosField.set(fragment, stubs);
    }

    @Test
    public void actualizarIcono_nombreExistenteAsignarRecurso() throws Exception {
        // Invocar actualizarIcono(1, "Reinforce")
        Method method = UploadFragment.class
                .getDeclaredMethod("actualizarIcono", int.class, String.class);
        method.setAccessible(true);
        method.invoke(fragment, 1, "Reinforce");

        // Verificar que stub[1].lastResource == 222
        StubImageView[] iconos = (StubImageView[]) UploadFragment.class
                .getDeclaredField("iconosStrats").get(fragment);
        assertEquals(Integer.valueOf(222), iconos[1].lastResource);
        // Asegurarse de que drawableCleared sea false
        assertEquals(false, iconos[1].drawableCleared);
    }

    @Test
    public void actualizarIcono_nombreNoExistenteLimpiarDrawable() throws Exception {
        // Invocar actualizarIcono(2, "Unknown")
        Method method = UploadFragment.class
                .getDeclaredMethod("actualizarIcono", int.class, String.class);
        method.setAccessible(true);
        method.invoke(fragment, 2, "Unknown");

        // Verificar que stub[2].drawableCleared == true y lastResource == null
        StubImageView[] iconos = (StubImageView[]) UploadFragment.class
                .getDeclaredField("iconosStrats").get(fragment);
        assertEquals(true, iconos[2].drawableCleared);
        assertNull(iconos[2].lastResource);
    }

    @Test
    public void actualizarIcono_noAfectaOtrosIndices() throws Exception {
        // Invocar actualizarIcono(0, "Supply Pack")
        Method method = UploadFragment.class
                .getDeclaredMethod("actualizarIcono", int.class, String.class);
        method.setAccessible(true);
        method.invoke(fragment, 0, "Supply Pack");

        StubImageView[] iconos = (StubImageView[]) UploadFragment.class
                .getDeclaredField("iconosStrats").get(fragment);


        assertEquals(Integer.valueOf(333), iconos[0].lastResource);

        for (int i = 1; i < 4; i++) {
            assertNull(iconos[i].lastResource);
            assertEquals(false, iconos[i].drawableCleared);
        }
    }
}
