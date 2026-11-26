package pe.edu.unap.holamovil;

import android.app.Activity;
import android.os.Bundle;
import android.graphics.Color;
import android.text.InputType;
import android.view.Gravity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
    private int contador = 0;
    private int indiceColor = 0;
    private final int[] colores = { Color.WHITE,
        Color.rgb(255, 235, 238), Color.rgb(232, 245, 233),
        Color.rgb(227, 242, 253) };
    private TextView texto, resultado;
    private EditText num1, num2;
    private LinearLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            contador = savedInstanceState.getInt("contador");
            indiceColor = savedInstanceState.getInt("color");
        }
        ScrollView scroll = new ScrollView(this);
        scroll.setFillViewport(true);
        scroll.setOnApplyWindowInsetsListener((v, insets) -> {
            v.setPadding(0, insets.getSystemWindowInsetTop(),
                0, insets.getSystemWindowInsetBottom());
            return insets;
        });
        layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setGravity(Gravity.CENTER_HORIZONTAL);
        int espacio = (int) (20 * getResources().getDisplayMetrics().density);
        layout.setPadding(espacio, espacio, espacio, espacio);
        layout.setBackgroundColor(colores[indiceColor]);
        scroll.addView(layout);

        titulo("Hola Móvil · Java / Android");
        titulo("01 · Contador y saludo");
        texto = new TextView(this);
        texto.setTextSize(24);
        texto.setGravity(Gravity.CENTER);
        texto.setText(contador == 0 ? "¡Hola desde Puno!" :
            "Tocaste " + contador + " veces");
        layout.addView(texto);
        LinearLayout fila = fila();
        boton(fila, "Tócame", () -> {
            contador++;
            texto.setText("Tocaste " + contador + " veces");
        });
        boton(fila, "Reiniciar", () -> {
            contador = 0;
            texto.setText("¡Hola desde Puno!");
        });
        EditText nombre = campo("Escribe tu nombre", false);
        boton(layout, "Saludar", () -> {
            String n = nombre.getText().toString().trim();
            texto.setText(n.isEmpty() ? "Primero escribe un nombre" :
                "¡Hola, " + n + "!");
        });

        titulo("02 · Calculadora");
        num1 = campo("Primer número", true);
        num2 = campo("Segundo número", true);
        LinearLayout operaciones = fila();
        boton(operaciones, "Sumar", () -> calcular('+'));
        boton(operaciones, "Restar", () -> calcular('-'));
        LinearLayout operaciones2 = fila();
        boton(operaciones2, "Multiplicar", () -> calcular('*'));
        boton(operaciones2, "Dividir", () -> calcular('/'));
        resultado = new TextView(this);
        resultado.setTextSize(20);
        resultado.setGravity(Gravity.CENTER);
        resultado.setText("Resultado: —");
        layout.addView(resultado);

        titulo("03 · Color de fondo");
        boton(layout, "Cambiar color", () -> {
            indiceColor = (indiceColor + 1) % colores.length;
            layout.setBackgroundColor(colores[indiceColor]);
            Toast.makeText(this, "Color " + (indiceColor + 1) + " de " +
                colores.length, Toast.LENGTH_SHORT).show();
        });
        setContentView(scroll);
    }

    private void calcular(char operacion) {
        try {
            double a = Double.parseDouble(num1.getText().toString().trim());
            double b = Double.parseDouble(num2.getText().toString().trim());
            if (Double.isNaN(a) || Double.isInfinite(a) ||
                Double.isNaN(b) || Double.isInfinite(b)) {
                resultado.setText("Ingresa dos números válidos");
                return;
            }
            if (operacion == '/' && b == 0) {
                resultado.setText("No se puede dividir entre cero");
                return;
            }
            double valor;
            switch (operacion) {
                case '-': valor = a - b; break;
                case '*': valor = a * b; break;
                case '/': valor = a / b; break;
                default: valor = a + b;
            }
            resultado.setText("Resultado: " + valor);
        } catch (NumberFormatException e) {
            resultado.setText("Ingresa dos números válidos");
        }
    }

    private LinearLayout fila() {
        LinearLayout f = new LinearLayout(this);
        f.setOrientation(LinearLayout.HORIZONTAL);
        layout.addView(f, new LinearLayout.LayoutParams(-1, -2));
        return f;
    }

    private void boton(LinearLayout padre, String etiqueta, Runnable accion) {
        Button b = new Button(this);
        b.setText(etiqueta);
        b.setAllCaps(false);
        b.setOnClickListener(v -> accion.run());
        padre.addView(b, padre == layout ? new LinearLayout.LayoutParams(-1, -2)
            : new LinearLayout.LayoutParams(0, -2, 1));
    }

    private EditText campo(String pista, boolean numerico) {
        EditText campo = new EditText(this);
        campo.setHint(pista);
        campo.setSingleLine(true);
        if (numerico) campo.setInputType(InputType.TYPE_CLASS_NUMBER |
            InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_NUMBER_FLAG_SIGNED);
        layout.addView(campo, new LinearLayout.LayoutParams(-1, -2));
        return campo;
    }

    private void titulo(String valor) {
        TextView t = new TextView(this);
        t.setText(valor);
        t.setTextSize(14);
        t.setTextColor(Color.rgb(55, 71, 79));
        t.setPadding(0, 16, 0, 8);
        layout.addView(t);
    }

    @Override
    protected void onSaveInstanceState(Bundle estado) {
        super.onSaveInstanceState(estado);
        estado.putInt("contador", contador);
        estado.putInt("color", indiceColor);
    }
}
