package com.example.helldiversbuildhub;

import static org.junit.Assert.assertEquals;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Method;

public class MajorOrderAdapterTest {

    private MajorOrderAdapter adapter;
    private Method formatMethod;

    @Before
    public void setUp() throws Exception {

        adapter = new MajorOrderAdapter(null, null);

        formatMethod = MajorOrderAdapter.class.getDeclaredMethod("formatSecondsToDHm", int.class);
        formatMethod.setAccessible(true);
    }

    @Test
    public void testZeroSeconds() throws Exception {
        String resultado = (String) formatMethod.invoke(adapter, 0);
        Assert.assertEquals("0m", resultado);
    }

    @Test
    public void testOnlyMinutes() throws Exception {
        // 45 segundos > 0d 0h 0m => “0m”
        String r1 = (String) formatMethod.invoke(adapter, 45);
        Assert.assertEquals("0m", r1);

        // 120 segundos = 2 minutos
        String r2 = (String) formatMethod.invoke(adapter, 120);
        Assert.assertEquals("2m", r2);

        // 3599 segundos = 59 minutos
        String r3 = (String) formatMethod.invoke(adapter, 3599);
        Assert.assertEquals("59m", r3);
    }

    @Test
    public void testOnlyHours() throws Exception {
        // 3600 segundos = 1 hora
        String r1 = (String) formatMethod.invoke(adapter, 3600);
        Assert.assertEquals("1h", r1);

        // 3 600 x 5 = 18000 → 5 horas
        String r2 = (String) formatMethod.invoke(adapter, 5 * 3600);
        Assert.assertEquals("5h", r2);

        // 3 600 x 2 + 120 segundos = 2 horas y 2 minutos
        String r3 = (String) formatMethod.invoke(adapter, 2 * 3600 + 120);
        Assert.assertEquals("2h 2m", r3);
    }

    @Test
    public void testOnlyDays() throws Exception {
        // 86400 segundos = 1 día
        String r1 = (String) formatMethod.invoke(adapter, 86400);
        Assert.assertEquals("1d", r1);

        // 86400 x 3 = 3 días
        String r2 = (String) formatMethod.invoke(adapter, 3 * 86400);
        Assert.assertEquals("3d", r2);
    }

    @Test
    public void testDaysHoursMinutes() throws Exception {
        // 1 día, 1 hora, 1 minuto = 86400 + 3600 + 60 = 90060
        String r1 = (String) formatMethod.invoke(adapter, 86400 + 3600 + 60);
        Assert.assertEquals("1d 1h 1m", r1);

        // 2 días, 5 horas, 0 minutos = 2*86400 + 5 x 3600 = 183600
        String r2 = (String) formatMethod.invoke(adapter, 2 * 86400 + 5 * 3600);
        Assert.assertEquals("2d 5h", r2);

        // 0 días, 0 horas, 30 minutos = 1800
        String r3 = (String) formatMethod.invoke(adapter, 1800);
        Assert.assertEquals("30m", r3);
    }

    @Test
    public void testTrimmingSpaces() throws Exception {
        String r1 = (String) formatMethod.invoke(adapter, 86400 + 0 + 0);
        Assert.assertEquals("1d", r1);

        String r2 = (String) formatMethod.invoke(adapter, 3600 + 0);
        Assert.assertEquals("1h", r2);

        String r3 = (String) formatMethod.invoke(adapter, 60 + 0);
        Assert.assertEquals("1m", r3);
    }


}
