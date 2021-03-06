package proyecto.codigo.acceso;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.UploadNotificationConfig;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;
import eu.janmuller.android.simplecropimage.CropImage;


public class Fragment_Edit_Hole extends Fragment {


    EditText descripcion;
    EditText metros;
    EditText par;

    String _id_campo;
    String _nombre_hoyo;
    String _descripcion;
    String _metros;
    String _par;
    String _creador;
    String _url;

    Button registrar_button;
    Button cancelar_button;

    String ip_config;

    String URL;
    String URL1;


    JSONParser jsonParser=new JSONParser();

    View v;

    CircleImageView image;
    private File mFileTemp;
    public static final int REQUEST_CODE_CROP_IMAGE   = 0x3;
    public static final String TEMP_PHOTO_FILE_NAME = "temp_photo.jpg";
    public static final int REQUEST_CODE_GALLERY      = 0x1;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        super.onCreateView(inflater, container, savedInstanceState);
        Bundle bundle = getArguments();

        ip_config=getResources().getString(R.string.ip_config);
        URL="http://"+ip_config+"/TFG/BD/edit-hole.php";
        URL1="http://"+ip_config+"/TFG/BD/upload_circle.php";

        _id_campo=bundle.getString("id_campo");
        _nombre_hoyo=bundle.getString("nombre_hoyo");
        _descripcion=bundle.getString("descripcion");
        _metros=bundle.getString("metros");
        _par=bundle.getString("par");
        _creador=bundle.getString("creador");
        _url=bundle.getString("url");

        v=inflater.inflate(R.layout.signupscreen_hole_edit, container, false);

        descripcion=v.findViewById(R.id.signup_hole_des_text_edit);
        metros=v.findViewById(R.id.signup_hole_metros_text_edit);
        par=v.findViewById(R.id.signup_hole_par_text_edit);

        registrar_button=v.findViewById(R.id.crear_hole_button_edit);
        cancelar_button=v.findViewById(R.id.cancelar_hole_button_edit);

        image=v.findViewById(R.id.hole_profile_image_edit);


        registrar_button.setText("Modificar");


        descripcion.setText(_descripcion);
        metros.setText(_metros);
        par.setText(_par);

        if(_url!="null")
        {
            Picasso.get().load(_url).into(image);
        }


        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            mFileTemp = new File(Environment.getExternalStorageDirectory(), TEMP_PHOTO_FILE_NAME);
        }
        else {
            mFileTemp = new File(getActivity().getFilesDir(), TEMP_PHOTO_FILE_NAME);
        }


        image.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, REQUEST_CODE_GALLERY);
            }
        });


        cancelar_button.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                FragmentManager fm=getActivity().getSupportFragmentManager();
                Fragment_View_Hole fvh=new Fragment_View_Hole();
                final Bundle bundle = new Bundle();
                bundle.putString("id_campo", _id_campo);
                bundle.putString("nombre", _nombre_hoyo);
                bundle.putString("creador", _creador);
                fvh.setArguments(bundle);
                fm.beginTransaction().replace(R.id.contenedor, fvh).commit();
            }
        });



        registrar_button.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {


                if (descripcion.getText().toString().length()==0 || metros.getText().toString().length()==0 ||
                        par.getText().toString().length()==0)
                {
                    Toast.makeText(getActivity().getApplicationContext(), R.string.rellenar_datos, Toast.LENGTH_LONG).show();
                }
                else if(descripcion.getText().toString().trim().length()<2 || descripcion.getText().toString().trim().length()>250)
                {
                    Toast.makeText(getActivity().getApplicationContext(), "La descripción debe tener entre 2 y 250 caracteres", Toast.LENGTH_LONG).show();
                }
                else if(Integer.parseInt(metros.getText().toString().trim())<20 || Integer.parseInt(metros.getText().toString().trim())>1000)
                {
                    Toast.makeText(getActivity().getApplicationContext(), "El hoyo debe tener entre 20 y 1000 metros", Toast.LENGTH_LONG).show();
                }
                else if(Integer.parseInt(par.getText().toString().trim())<1 || Integer.parseInt(par.getText().toString().trim())>10)
                {
                    Toast.makeText(getActivity().getApplicationContext(), "El par del hoyo debe estar entre 1 y 10", Toast.LENGTH_LONG).show();
                }

                else
                {
                    AttemptEditHole attemptCreateHole = new AttemptEditHole();
                    attemptCreateHole.execute(descripcion.getText().toString().trim(),
                            metros.getText().toString().trim(), par.getText().toString().trim());

                    uploadMultipart();
                }
            }

        });

        return v;
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode != getActivity().RESULT_OK) {

            return;
        }

        Bitmap bitmap;

        switch (requestCode) {

            case REQUEST_CODE_GALLERY:

                try {

                    InputStream inputStream = getActivity().getContentResolver().openInputStream(data.getData());
                    FileOutputStream fileOutputStream = new FileOutputStream(mFileTemp);
                    copyStream(inputStream, fileOutputStream);
                    fileOutputStream.close();
                    inputStream.close();

                    startCropImage();

                } catch (Exception e) {

                    //Log.e(TAG, "Error while creating temp file", e);
                }

                break;
           /* case REQUEST_CODE_TAKE_PICTURE:

                startCropImage();
                break;*/
            case REQUEST_CODE_CROP_IMAGE:

                String path = data.getStringExtra(CropImage.IMAGE_PATH);
                if (path == null) {

                    return;
                }

                bitmap = BitmapFactory.decodeFile(mFileTemp.getPath());
                image.setImageBitmap(bitmap);


                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    private void startCropImage() {

        Intent intent = new Intent(getActivity(), CropImage.class);
        intent.putExtra(CropImage.IMAGE_PATH, mFileTemp.getPath());
        intent.putExtra(CropImage.SCALE, true);

        intent.putExtra(CropImage.ASPECT_X, 2);
        intent.putExtra(CropImage.ASPECT_Y, 2);

        startActivityForResult(intent, REQUEST_CODE_CROP_IMAGE);
    }

    public static void copyStream(InputStream input, OutputStream output) throws IOException {

        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = input.read(buffer)) != -1) {
            output.write(buffer, 0, bytesRead);
        }
    }


    public void uploadMultipart() {

        String id="";
        id=_id_campo;


        //if(mImageCaptureUri!=null)mFileTemp.getPath()
        if(mFileTemp.getPath()!=null)
        {
            String path="";


            //en principio, esto es sin comentar
            //path=getPath(mImageCaptureUri);

            //Uploading code
            try {
                String uploadId = UUID.randomUUID().toString();

                    new MultipartUploadRequest(getActivity(), uploadId, URL1)
                            //.addFileToUpload(path, "image") //Adding file
                            .addFileToUpload(mFileTemp.getPath(), "image")
                            .addParameter("id_campo", _id_campo) //Adding text parameter to the request
                            .addParameter("nombre_hoyo", _nombre_hoyo)
                            .setNotificationConfig(new UploadNotificationConfig())
                            .setMaxRetries(2)
                            .startUpload(); //Starting the upload

            } catch (Exception exc) {
                Toast.makeText(getActivity(), exc.getMessage(), Toast.LENGTH_SHORT).show();

            }

        }

    }



    //method to get the file path from uri
    public String getPath(Uri uri) {
        Cursor cursor = getActivity().getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
        cursor.close();

        cursor = getActivity().getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
        cursor.moveToFirst();
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
        cursor.close();

        return path;
    }
    //////////////////////////////////////////////////////////////////////////////

    private class AttemptEditHole extends AsyncTask<String, String, JSONObject> {

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
        }

        @Override
        protected JSONObject doInBackground(String... args)
        {
            String id_campo=_id_campo;
            String nombre_hoyo= _nombre_hoyo;
            String descripcion = args[0];
            String metros = args[1];
            String par = args[2];

            ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("id_campo",  StringUtils.stripAccents(id_campo)));
            params.add(new BasicNameValuePair("nombre_hoyo",  StringUtils.stripAccents(nombre_hoyo)));
            params.add(new BasicNameValuePair("descripcion",  StringUtils.stripAccents(descripcion)));
            params.add(new BasicNameValuePair("metros",  StringUtils.stripAccents(metros)));
            params.add(new BasicNameValuePair("par",  StringUtils.stripAccents(par)));
            JSONObject json = jsonParser.makeHttpRequest(URL, "POST", params);

            return json;
        }

        protected void onPostExecute(JSONObject result)
        {
            try
            {
                if(result != null)
                {
                    Toast.makeText(getActivity().getApplicationContext(),result.getString("message"),Toast.LENGTH_LONG).show();
                    FragmentManager fm=getActivity().getSupportFragmentManager();
                    Fragment_View_Hole fvh=new Fragment_View_Hole();
                    final Bundle bundle = new Bundle();
                    bundle.putString("id_campo", _id_campo);
                    bundle.putString("nombre", _nombre_hoyo);
                    bundle.putString("creador", _creador);
                    fvh.setArguments(bundle);
                    fm.beginTransaction().replace(R.id.contenedor, fvh).commit();
                }
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
        }
    }
}
