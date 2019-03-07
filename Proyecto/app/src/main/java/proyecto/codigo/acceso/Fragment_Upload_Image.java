package proyecto.codigo.acceso;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.UploadNotificationConfig;
import java.io.IOException;
import java.util.UUID;

public class Fragment_Upload_Image extends Fragment {


    //Declaring views
    private Button buttonChoose;
    private Button buttonUpload;
    private ImageView imageView;
    private EditText editText;

    //Image request code
    private int PICK_IMAGE_REQUEST = 1;

    //storage permission code
    private static final int STORAGE_PERMISSION_CODE = 123;

    //Bitmap to get image from gallery
    private Bitmap bitmap;

    //Uri to store the image uri
    private Uri filePath;

    View v;

    String ip_config;
    String URL;
    String id_campo;
    String tipo;
    String nombre_hoyo;
    String username;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        super.onCreateView(inflater, container, savedInstanceState);

        Bundle bundle = getArguments();

        tipo=bundle.getString("tipo");

        username=bundle.getString("username");

        if(tipo=="campo")
        {
            id_campo=bundle.getString("id_campo");
        }

        else if(tipo=="hoyo")
        {
            id_campo=bundle.getString("id_campo");
            nombre_hoyo=bundle.getString("nombre_hoyo");
        }


        v=inflater.inflate(R.layout.fragment_upload_image, container, false);

        //Requesting storage permission
        requestStoragePermission();

        ip_config=getResources().getString(R.string.ip_config);
        URL="http://"+ip_config+"/TFG/BD/upload.php";

        //Initializing views
        buttonChoose = v.findViewById(R.id.buttonChooseUpload);
        buttonUpload = v.findViewById(R.id.buttonUpload);
        imageView = v.findViewById(R.id.imageViewUpload);
        editText = v.findViewById(R.id.editTextNameUpload);

        buttonChoose.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                showFileChooser();
            }
        });
        buttonUpload.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                uploadMultipart();

                if(tipo=="campo")
                {
                    FragmentManager fm=getActivity().getSupportFragmentManager();
                    Fragment_View_Field fvf=new Fragment_View_Field();
                    final Bundle bundle = new Bundle();
                    bundle.putString("id", id_campo);
                    fvf.setArguments(bundle);
                    fm.beginTransaction().replace(R.id.contenedor, fvf).commit();
                }

                else if(tipo=="hoyo")
                {
                    FragmentManager fm=getActivity().getSupportFragmentManager();
                    Fragment_View_Hole fvh=new Fragment_View_Hole();
                    final Bundle bundle = new Bundle();
                    bundle.putString("id_campo", id_campo);
                    bundle.putString("nombre", nombre_hoyo);
                    fvh.setArguments(bundle);
                    fm.beginTransaction().replace(R.id.contenedor, fvh).commit();
                }

                else
                {
                    FragmentManager fm=getActivity().getSupportFragmentManager();
                    Fragment_View_Profile fvp=new Fragment_View_Profile();
                    fm.beginTransaction().replace(R.id.contenedor, fvp).commit();
                }
            }
        });

        return v;

    }


    /*
     * This is the method responsible for image upload
     * We need the full image path and the name for the image in this method
     * */
    public void uploadMultipart() {
        //getting comment for the image
        String comentario = editText.getText().toString().trim();

        //getting the actual path of the image
        String path = getPath(filePath);

       //Uploading code
        try {
            String uploadId = UUID.randomUUID().toString();

            //Creating a multi part request

            if(tipo=="campo")
            {
                new MultipartUploadRequest(getActivity(), uploadId, URL)
                        .addFileToUpload(path, "image") //Adding file
                        .addParameter("comentario", comentario) //Adding text parameter to the request
                        .addParameter("username", username)
                        .addParameter("id_campo", id_campo)
                        .setNotificationConfig(new UploadNotificationConfig())
                        .setMaxRetries(2)
                        .startUpload(); //Starting the upload
            }

            else if(tipo=="hoyo")
            {

                new MultipartUploadRequest(getActivity(), uploadId, URL)
                        .addFileToUpload(path, "image") //Adding file
                        .addParameter("comentario", comentario) //Adding text parameter to the request
                        .addParameter("username", username)
                        .addParameter("id_campo", id_campo)
                        .addParameter("nombre_hoyo", nombre_hoyo)
                        .setNotificationConfig(new UploadNotificationConfig())
                        .setMaxRetries(2)
                        .startUpload(); //Starting the upload
            }

            else
            {
                new MultipartUploadRequest(getActivity(), uploadId, URL)
                        .addFileToUpload(path, "image") //Adding file
                        .addParameter("comentario", comentario) //Adding text parameter to the request
                        .addParameter("username", username)
                        .setNotificationConfig(new UploadNotificationConfig())
                        .setMaxRetries(2)
                        .startUpload(); //Starting the upload
            }


        } catch (Exception exc) {
            Toast.makeText(getActivity(), exc.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    //method to show file chooser
    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    //handling the image chooser activity result
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), filePath);
                imageView.setImageBitmap(bitmap);

            } catch (IOException e) {
                e.printStackTrace();
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
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
        cursor.moveToFirst();
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
        cursor.close();

        return path;
    }


    //Requesting permission
    private void requestStoragePermission() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
            return;

        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)) {
            //If the user has denied the permission previously your code will come to this block
            //Here you can explain why you need this permission
            //Explain here why you need this permission
        }
        //And finally ask for the permission
        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
    }

    //This method will be called when the user will tap on allow or deny
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        //Checking the request code of our request
        if (requestCode == STORAGE_PERMISSION_CODE) {

            //If permission is granted
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Displaying a toast
                Toast.makeText(getActivity(), "Permission granted now you can read the storage", Toast.LENGTH_LONG).show();
            } else {
                //Displaying another toast if permission is not granted
                Toast.makeText(getActivity(), "Oops you just denied the permission", Toast.LENGTH_LONG).show();
            }
        }
    }
}