package mx.edu.ittepic.dadm_u4_practica1_recetasdecocina_guillermoemiliomedinamadera;

import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity
{
    EditText identificador, nombre, ingredientes, preparacion, observaciones;
    Button insertar, consultar, eliminar, actualizar;

    BaseDatos base;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        identificador = findViewById(R.id.identificador);
        nombre = findViewById(R.id.nombre);
        ingredientes = findViewById(R.id.ingredientes);
        preparacion = findViewById(R.id.preparacion);
        observaciones = findViewById(R.id.observaciones);

        insertar = findViewById(R.id.insertar);
        consultar = findViewById(R.id.consultar);
        eliminar = findViewById(R.id.eliminar);
        actualizar = findViewById(R.id.actualizar);

        base = new BaseDatos(this, "recetas1", null, 1);

        insertar.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Insertar();
            }
        });

        consultar.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                pedirId(1);
            }
        });

        actualizar.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                pedirId(2);
            }
        });

        eliminar.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                pedirId(3);
            }
        });

    }





    private void invocarConfirmacionActualizacion()
    {
        AlertDialog.Builder confir = new AlertDialog.Builder(this);

        confir.setTitle("IMPORTANTE").setMessage("¿Estas seguro que deseas aplicar los cambios?")
                .setPositiveButton("Si", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        aplicarActualizacion();
                        dialog.dismiss();
                    }
                }).setNegativeButton("Cancelar", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                habilitarBotonesyLimpiarCampos();
                dialog.cancel();
            }
        }).show();
    }

    private void habilitarBotonesyLimpiarCampos()
    {
        identificador.setText("");
        nombre.setText("");
        ingredientes.setText("");
        preparacion.setText("");
        observaciones.setText("");

        insertar.setEnabled(true);
        consultar.setEnabled(true);
        eliminar.setEnabled(true);
        actualizar.setText("Actualizar");
        identificador.setEnabled(true);

    }

    private void aplicarActualizacion()
    {
        try
        {
            SQLiteDatabase tabla = base.getWritableDatabase();

            String SQL = "UPDATE RECETAS SET NOMBRE = '" + nombre.getText().toString() + "', INGREDIENTES = '" + ingredientes.getText().toString() +
                    "' , PREPARACION ='" + preparacion.getText().toString() + "' , OBSERVACIONES = '" + observaciones.getText().toString()
                     + "'  WHERE ID=" + identificador.getText().toString();

            tabla.execSQL(SQL);
            tabla.close();

            Toast.makeText(this, "Se actualizaron correctamente los datos",Toast.LENGTH_LONG).show();

        }
        catch (SQLiteException e)
        {
            Toast.makeText(this, "No se pudo",Toast.LENGTH_LONG).show();
        }

        habilitarBotonesyLimpiarCampos();
    }



    private void confirmarEliminar(final String idEliminar)
    {
        String confirmacion = "";
        try
        {
            SQLiteDatabase tabla = base.getReadableDatabase();

            String SQL = "SELECT * FROM RECETAS WHERE ID="+idEliminar;

            Cursor resultado = tabla.rawQuery(SQL, null);

            if (resultado.moveToFirst())
            {
                //si hay resultado
                confirmacion = "Seguro que desea eliminar la receta: " + resultado.getString(1);

                //Toast.makeText(this,confirmacion, Toast.LENGTH_LONG).show();

            }
            else
            {
                //no hay
                Toast.makeText(this,"no existe ese valor", Toast.LENGTH_LONG).show();
            }

            tabla.close();

        }
        catch (SQLiteException e)
        {
            Toast.makeText(this,"no se pudo buscar", Toast.LENGTH_LONG).show();
        }


        AlertDialog.Builder alerta = new AlertDialog.Builder(this);
        alerta.setTitle("ATENCION").setMessage(confirmacion).
                setPositiveButton("Eliminiar", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        eliminarDato(idEliminar);
                        dialog.dismiss();
                    }
                }).
                setNegativeButton("Cancelar", null).show();

    }

    private void eliminarDato(String idAEliminar)
    {
        try
        {
            SQLiteDatabase tabla = base.getReadableDatabase();


            String SQL = "DELETE FROM RECETAS WHERE ID="+idAEliminar;

            tabla.execSQL(SQL);

            tabla.close();

            habilitarBotonesyLimpiarCampos();

        }
        catch (SQLiteException e)
        {
            Toast.makeText(this,"no se pudo buscar", Toast.LENGTH_LONG).show();
        }
    }

    private void pedirId(final int origen)
    {
        final EditText pidoID = new EditText(this);
        String mensaje = "Escriba el ID a buscar: ";
        String boton = "Buscar";

        pidoID.setInputType(InputType.TYPE_CLASS_NUMBER);
        pidoID.setHint("Valor entero mayor de 0");

        AlertDialog.Builder alerta = new AlertDialog.Builder(this);

        if (origen == 2 )
        {
            mensaje = "Escriba el ID a modificar: ";
            boton = "Modificar";
            pidoID.setText(identificador.getText());
            pidoID.setFocusable(false);
        }

        if (origen == 3)
        {
            mensaje = "Escriba el ID a eliminar: ";
            boton = "Eliminar";
            pidoID.setText(identificador.getText());
            pidoID.setFocusable(false);
        }

        alerta.setTitle("ATENCION").setMessage(mensaje).setView(pidoID).
                setPositiveButton(boton, new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        if (pidoID.getText().toString().isEmpty())
                        {
                            Toast.makeText(MainActivity.this, "Debes escribir un valor",Toast.LENGTH_LONG).show();
                            return;
                        }

                        if (origen == 1 )
                        {
                            buscarDato(pidoID.getText().toString(), origen);
                        }

                        if (origen == 2 )
                        {
                            invocarConfirmacionActualizacion();
                        }

                        if (origen == 3)
                        {
                            confirmarEliminar(pidoID.getText().toString());
                        }

                        dialog.dismiss();
                    }
                }).
                setNegativeButton("Cancelar", null).show();

    }

    private void buscarDato(String idABuscar, int origen)
    {
        try
        {
            SQLiteDatabase tabla = base.getReadableDatabase();

            String SQL = "SELECT * FROM RECETAS WHERE ID="+idABuscar;

            Cursor resultado = tabla.rawQuery(SQL, null);

            if (resultado.moveToFirst())
            {
                /*if (origen == 3)
                {
                    String datos = idABuscar + "&" + resultado.getString(1) + "&" + resultado.getString(2) +
                            "&" + resultado.getString(3);
                    confirmarEliminar(datos);
                    return;
                }*/


                identificador.setText(resultado.getString(0));
                nombre.setText(resultado.getString(1));
                ingredientes.setText(resultado.getString(2));
                preparacion.setText(resultado.getString(3));
                observaciones.setText(resultado.getString(4));

                /*if (origen == 2)
                {
                    insertar.setEnabled(false);
                    consultar.setEnabled(false);
                    eliminar.setEnabled(false);
                    actualizar.setText("Confirmar cambios");
                    identificador.setEnabled(false);
                }*/

            }
            else
            {
                //no hay
                Toast.makeText(this,"no existe ese valor", Toast.LENGTH_LONG).show();
            }

            tabla.close();

        }
        catch (SQLiteException e)
        {
            Toast.makeText(this,"no se pudo buscar", Toast.LENGTH_LONG).show();
        }
    }


    private void Insertar()
    {
        try
        {
            SQLiteDatabase tabla = base.getWritableDatabase();

            String SQL = "INSERT INTO RECETAS VALUES ("+identificador.getText().toString()+",'"+nombre.getText().toString()+"','"+
                            ingredientes.getText().toString()+"','"+preparacion.getText().toString()+"','" + observaciones.getText().toString() +"')";


            /*String SQL = "INSERT INTO RECETAS VALUES ( %1, '%2', '%3', '%4', '%5')";
            SQL = SQL.replace("%1", identificador.getText().toString());
            SQL = SQL.replace("%2", nombre.getText().toString());
            SQL = SQL.replace("%3", ingredientes.getText().toString());
            SQL = SQL.replace("%4", preparacion.getText().toString());
            SQL = SQL.replace("%5", observaciones.getText().toString());*/

            tabla.execSQL(SQL);
            Toast.makeText(this,"Si se pudo insertar", Toast.LENGTH_LONG).show();
            tabla.close();

            identificador.setText("");
            nombre.setText("");
            ingredientes.setText("");
            preparacion.setText("");
            observaciones.setText("");

        }
        catch (SQLiteException e)
        {
            Toast.makeText(this,"no se pudo insertar", Toast.LENGTH_LONG).show();
        }
    }




}
