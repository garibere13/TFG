package proyecto.codigo.acceso;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
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

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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







    private Uri mImageCaptureUri;
    private ImageView mImageView;
    private static final int PICK_FROM_CAMERA = 1;
    private static final int CROP_FROM_CAMERA = 2;
    private static final int PICK_FROM_FILE = 3;



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












        final String [] items			= new String [] {"Take from camera", "Select from gallery"};
        ArrayAdapter<String> adapter	= new ArrayAdapter<String> (getActivity(), android.R.layout.select_dialog_item,items);
        AlertDialog.Builder builder		= new AlertDialog.Builder(getActivity());
        builder.setTitle("Select Image");
        builder.setAdapter( adapter, new DialogInterface.OnClickListener() {
            public void onClick( DialogInterface dialog, int item ) { //pick from camera
                if (item == 0) {
                    Intent intent 	 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                    mImageCaptureUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(),
                            "tmp_avatar_" + String.valueOf(System.currentTimeMillis()) + ".jpg"));

                    intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, mImageCaptureUri);

                    try {
                        intent.putExtra("return-data", true);

                        startActivityForResult(intent, PICK_FROM_CAMERA);
                    } catch (ActivityNotFoundException e) {
                        e.printStackTrace();
                    }
                } else { //pick from file
                    Intent intent = new Intent();

                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);

                    startActivityForResult(Intent.createChooser(intent, "Complete action using"), PICK_FROM_FILE);
                }
            }
        } );
        final AlertDialog dialog = builder.create();

        Button button 	= (Button) v.findViewById(R.id.buttonChooseUpload);
        mImageView		= (ImageView) v.findViewById(R.id.imageViewUpload);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
            }
        });












        //Requesting storage permission
        requestStoragePermission();

        ip_config=getResources().getString(R.string.ip_config);
        URL="http://"+ip_config+"/TFG/BD/upload.php";

        //Initializing views
        buttonChoose = v.findViewById(R.id.buttonChooseUpload);
        buttonUpload = v.findViewById(R.id.buttonUpload);
        imageView = v.findViewById(R.id.imageViewUpload);
        editText = v.findViewById(R.id.editTextNameUpload);

      /*  buttonChoose.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                showFileChooser();


            }
        });*/
        buttonUpload.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                uploadMultipart();


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
        String comentario="";
        comentario=editText.getText().toString().trim();

        //getting the actual path of the image


        if(mImageCaptureUri!=null)
        {
            String path="";
            path=getPath(mImageCaptureUri);
            //Uploading code
            try {
                String uploadId = UUID.randomUUID().toString();

                //Creating a multi part request

                if (tipo == "campo") {
                    new MultipartUploadRequest(getActivity(), uploadId, URL)
                            .addFileToUpload(path, "image") //Adding file
                            .addParameter("comentario", comentario) //Adding text parameter to the request
                            .addParameter("username", username)
                            .addParameter("id_campo", id_campo)
                            .setNotificationConfig(new UploadNotificationConfig())
                            .setMaxRetries(2)
                            .startUpload(); //Starting the upload
                } else if (tipo == "hoyo") {

                    new MultipartUploadRequest(getActivity(), uploadId, URL)
                            .addFileToUpload(path, "image") //Adding file
                            .addParameter("comentario", comentario) //Adding text parameter to the request
                            .addParameter("username", username)
                            .addParameter("id_campo", id_campo)
                            .addParameter("nombre_hoyo", nombre_hoyo)
                            .setNotificationConfig(new UploadNotificationConfig())
                            .setMaxRetries(2)
                            .startUpload(); //Starting the upload
                } else {
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
                final Bundle bundle = new Bundle();
                bundle.putString("username", username);
                fvp.setArguments(bundle);
                fm.beginTransaction().replace(R.id.contenedor, fvp).commit();
            }

        }
        else
        {
            Toast.makeText(getActivity().getApplicationContext(),"Seleccione una foto", Toast.LENGTH_LONG).show();
            buttonChoose.setSelected(true);
        }
    }




    private void doCrop() {
        final ArrayList<CropOption> cropOptions = new ArrayList<CropOption>();

        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setType("image/*");

        List<ResolveInfo> list = getActivity().getPackageManager().queryIntentActivities( intent, 0 );

        int size = list.size();

        if (size == 0) {
            Toast.makeText(getActivity(), "Can not find image crop app", Toast.LENGTH_SHORT).show();

            return;
        } else {
            intent.setData(mImageCaptureUri);

            intent.putExtra("outputX", 50);
            intent.putExtra("outputY", 50);
            intent.putExtra("aspectX", 1);
            intent.putExtra("aspectY", 1);
            intent.putExtra("scale", true);
            intent.putExtra("return-data", true);

            if (size == 1) {
                Intent i 		= new Intent(intent);
                ResolveInfo res	= list.get(0);

                i.setComponent( new ComponentName(res.activityInfo.packageName, res.activityInfo.name));

                startActivityForResult(i, CROP_FROM_CAMERA);
            } else {
                for (ResolveInfo res : list) {
                    final CropOption co = new CropOption();

                    co.title 	= getActivity().getPackageManager().getApplicationLabel(res.activityInfo.applicationInfo);
                    co.icon		= getActivity().getPackageManager().getApplicationIcon(res.activityInfo.applicationInfo);
                    co.appIntent= new Intent(intent);

                    co.appIntent.setComponent( new ComponentName(res.activityInfo.packageName, res.activityInfo.name));

                    cropOptions.add(co);
                }

                CropOptionAdapter adapter = new CropOptionAdapter(getActivity().getApplicationContext(), cropOptions);

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Choose Crop App");
                builder.setAdapter( adapter, new DialogInterface.OnClickListener() {
                    public void onClick( DialogInterface dialog, int item ) {
                        startActivityForResult( cropOptions.get(item).appIntent, CROP_FROM_CAMERA);
                    }
                });

                builder.setOnCancelListener( new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel( DialogInterface dialog ) {

                        if (mImageCaptureUri != null ) {
                            getActivity().getContentResolver().delete(mImageCaptureUri, null, null );
                            mImageCaptureUri = null;
                        }
                    }
                } );

                AlertDialog alert = builder.create();

                alert.show();
            }
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
  /*  @Override
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
    }*/

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != getActivity().RESULT_OK) return;

        switch (requestCode) {
            case PICK_FROM_CAMERA:
                doCrop();

                break;

            case PICK_FROM_FILE:
                mImageCaptureUri = data.getData();

                doCrop();

                break;

            case CROP_FROM_CAMERA:
                Bundle extras = data.getExtras();

                if (extras != null) {
                    Bitmap photo = extras.getParcelable("data");

                    mImageView.setImageBitmap(photo);
                }

                File f = new File(mImageCaptureUri.getPath());

                if (f.exists()) f.delete();

                break;

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