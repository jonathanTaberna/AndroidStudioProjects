package hsfarmacia.farmaclub;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.github.barteksc.pdfviewer.PDFView;

import java.io.File;

public class PdfActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf);

        PDFView p = (PDFView)findViewById(R.id.pdfView);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            File pdfPath = new File(extras.getString("pdfPath"));
            p.fromFile(pdfPath).load();
        }

        /*
        Toast.makeText(this, "lalalalalaaalallalaallaa",Toast.LENGTH_SHORT).show();


        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            int id = extras.getInt("idNotification");
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.cancel(id);
        }
        */

        /*
        pdfPath = extras.getString("pdfPath");
        File pdfFile = new File(pdfPath);
        Uri path = null;

        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.M){
            path = Uri.fromFile(pdfFile);
        } else {
            path = Uri.parse(pdfPath);
        }

        Intent pdfIntent = new Intent(Intent.ACTION_VIEW);
        pdfIntent.setDataAndType(path, "application/pdf");
        pdfIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        pdfIntent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);


        try {
            startActivity(pdfIntent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(getBaseContext(),"No existe aplicacion para visualizar PDF.", Toast.LENGTH_SHORT).show();
        }
        */
    }
}
