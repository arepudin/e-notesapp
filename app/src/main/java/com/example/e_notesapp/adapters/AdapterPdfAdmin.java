package com.example.e_notesapp.adapters;

import static com.example.e_notesapp.Constants.MAX_BYTES_PDF;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.e_notesapp.MyApplication;
import com.example.e_notesapp.PdfDetailActivity;
import com.example.e_notesapp.PdfEditActivity;
import com.example.e_notesapp.databinding.RowPdfAdminBinding;
import com.example.e_notesapp.filters.FilterPdfAdmin;
import com.example.e_notesapp.models.ModelPdf;
import com.github.barteksc.pdfviewer.PDFView;

import java.util.ArrayList;
import java.util.Objects;

public class AdapterPdfAdmin extends RecyclerView.Adapter<AdapterPdfAdmin.HolderPdfAdmin> implements Filterable {

    // context
    private Context context;
    // arraylist to hold list of data of type ModelPdf
    public ArrayList<ModelPdf> pdfArrayList, filterList;

    // view binding row_pdf_admin.xml
    private RowPdfAdminBinding binding;

    private FilterPdfAdmin filter;

    private static final String TAG = "PDF_ADAPTER_TAG";

    // progress
    private AlertDialog progressDialog;

    // constructor
    public AdapterPdfAdmin(Context context, ArrayList<ModelPdf> pdfArrayList) {
        this.context = context;
        this.pdfArrayList = pdfArrayList;
        this.filterList = pdfArrayList;

        // init progress dialog
        progressDialog = new AlertDialog.Builder(context)
                .setTitle("Please wait")
                .setView(new ProgressBar(context))
                .setCancelable(false)
                .create();
    }

    @NonNull
    @Override
    public HolderPdfAdmin onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // bind layout using view binding
        binding = RowPdfAdminBinding.inflate(LayoutInflater.from(context), parent, false);
        return new HolderPdfAdmin(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterPdfAdmin.HolderPdfAdmin holder, int position) {
        // get data
        ModelPdf model = pdfArrayList.get(position);

        // set data using the setData method
        setData(holder, model);

        // handle clicks, show dialog with options: Edit, Delete
        holder.moreBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moreOptionsDialog(model, holder);
            }
        });

        // handle click on the whole item, open PdfDetailActivity
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PdfDetailActivity.class);
                intent.putExtra("bookId", model.getId());
                context.startActivity(intent);
            }
        });
    }

    // Add this method to set data in the ViewHolder
    private void setData(HolderPdfAdmin holder, ModelPdf model) {
        // Set data
        holder.titleTv.setText(model.getTitle());
        holder.descriptionTv.setText(model.getDescription());
        holder.dateTv.setText(MyApplication.formatTimestamp(model.getTimestamp()));

        // Load further details
        MyApplication.loadCategory(Objects.requireNonNull(model.getCategoryId()), holder.categoryTv);
        MyApplication.loadPdfFromUrlSinglePage(model.getUrl(), model.getTitle(), holder.pdfView, holder.progressBar);
        MyApplication.loadPdfSize(model.getUrl(), model.getTitle(), holder.sizeTv);
    }

    @Override
    public int getItemCount() {
        return pdfArrayList.size(); // return number of records | list size
    }

    @Override
    public Filter getFilter() {
        if (filter == null) {
            filter = new FilterPdfAdmin(filterList, this);
        }
        return filter;
    }

    /*View Holder class for row_pdf_admin.xml*/
    class HolderPdfAdmin extends RecyclerView.ViewHolder {

        // UI Views of row_pdf_admin.xml
        PDFView pdfView;
        ProgressBar progressBar;
        TextView titleTv, descriptionTv, categoryTv, sizeTv, dateTv;
        ImageButton moreBtn;

        // Constructor
        public HolderPdfAdmin(@NonNull View itemView) {
            super(itemView);

            // Init UI views
            pdfView = binding.pdfView;
            progressBar = binding.progressBar;
            titleTv = binding.titleTv;
            descriptionTv = binding.descriptionTv;
            categoryTv = binding.categoryTv;
            sizeTv = binding.sizeTv;
            dateTv = binding.dateTv;
            moreBtn = binding.moreBtn;
        }
    }

    private void moreOptionsDialog(ModelPdf model, HolderPdfAdmin holder) {
        String bookId = model.getId();
        String bookUrl = model.getUrl();
        String bookTitle = model.getTitle();

        // options to show in dialog
        String[] options = {"Edit", "Delete"};

        // Alert dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Choose Options")
                .setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // handle dialog option click
                        if (which == 0) {
                            // Edit clicked, open new activity to edit the book info
                            Intent intent = new Intent(context, PdfEditActivity.class);
                            intent.putExtra("bookId", bookId);
                            context.startActivity(intent);
                        } else if (which == 1) {
                            // Delete Clicked
                            MyApplication.deleteBook(
                                    context,
                                    "" + bookId,
                                    "" + bookUrl,
                                    "" + bookTitle
                            );
                        }
                    }
                })
                .show();
    }
}



