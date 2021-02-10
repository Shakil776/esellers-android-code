package com.bd.esellers.mainApp.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.InputFilter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.bd.esellers.R;
import com.bd.esellers.category.categoryModel.Category;
import com.bd.esellers.database.DatabaseQuery;
import com.bd.esellers.interfaces.CategoryListener;
import com.bd.esellers.mainApp.dataModel.Footer;
import com.bd.esellers.mainApp.dataModel.HorizontalModel;
import com.bd.esellers.mainApp.dataModel.RecyclerViewItem;
import com.bd.esellers.mainApp.dataModel.SliderImage;
import com.bd.esellers.mainApp.dataModel.VerticalModel;

import java.util.ArrayList;
import java.util.List;

import com.bd.esellers.utility.ImageHelper;

public class MixedAdapter extends RecyclerView.Adapter {

    private String TAG="MixedAdapter ";

    private List<RecyclerViewItem> recyclerViewItems;

    private List<SliderImage> sliders = new ArrayList<>();
    //Header Item Type
    private static final int HEADER_ITEM = 0;
    //Footer Item Type
    private static final int FOOTER_ITEM = 1;
    // Item Type
    private static final int PRODUCT_ITEM = 2;

    private static final int CATEGORY_ITEM = 3;
    private Context context;
    private SliderImage slider = new SliderImage();

    private MixedAdapterAction action;

    public MixedAdapter(List<RecyclerViewItem> recyclerViewItems, Context context) {
        this.recyclerViewItems = recyclerViewItems;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View row;
        //Check fot view Type inflate layout according to it
        if (viewType == HEADER_ITEM) {
            row = inflater.inflate(R.layout.row_header, parent, false);
            return new HeaderHolder(row);
        } else if (viewType == FOOTER_ITEM) {
            row = inflater.inflate(R.layout.row_footer, parent, false);
            return new FooterHolder(row);
        } else if (viewType == PRODUCT_ITEM) {
            row = inflater.inflate(R.layout.vertical_row, parent, false);
            return new VerticalViewHolder(row);
        } else if (viewType == CATEGORY_ITEM) {
            row = inflater.inflate(R.layout.category_row, parent, false);
            return new CategoryHolder(row);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        RecyclerViewItem recyclerViewItem = recyclerViewItems.get(position);
        //Check holder instance to populate data  according to it
        if (holder instanceof HeaderHolder) {
            HeaderHolder headerHolder = (HeaderHolder) holder;
            headerHolder.viewFlipper.startFlipping();
            Log.e("if header", "onBindViewHolder: " + slider.getSliderImage());

        } else if (holder instanceof FooterHolder) {
            FooterHolder footerHolder = (FooterHolder) holder;
            Footer footer = (Footer) recyclerViewItem;
            //set data
            footerHolder.texViewQuote.setText(footer.getQuote());
            footerHolder.textViewAuthor.setText(footer.getAuthor());
            Picasso.get().load(footer.getImageUrl()).into(footerHolder.imageViewFooter);

        } else if (holder instanceof VerticalViewHolder) {

            VerticalViewHolder verticalViewHolder = (VerticalViewHolder) holder;
            VerticalModel verticalModel = (VerticalModel) recyclerViewItem;
            verticalViewHolder.catTV.setText(verticalModel.getHorizontalModels().get(0).getStatus());

            ArrayList<HorizontalModel> hmList = verticalModel.getHorizontalModels();

            verticalViewHolder.horizontalRV.setHasFixedSize(true);
            HorizontalAdapter horizontalAdapter = new HorizontalAdapter(context, hmList);
            verticalViewHolder.horizontalRV.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
            verticalViewHolder.horizontalRV.setAdapter(horizontalAdapter);

        } else if (holder instanceof CategoryHolder) {
            CategoryHolder categoryHolder = (CategoryHolder) holder;

        }
    }

    @Override
    public int getItemViewType(int position) {
        //here we can set view type
        RecyclerViewItem recyclerViewItem = recyclerViewItems.get(position);
        //if its header then return header item
        if (recyclerViewItem instanceof SliderImage)
            return HEADER_ITEM;
            //if its Footer then return Footer item
        else if (recyclerViewItem instanceof Footer)
            return FOOTER_ITEM;
            //if its FoodItem then return Food item
        else if (recyclerViewItem instanceof VerticalModel)
            return PRODUCT_ITEM;
        else if (recyclerViewItem instanceof Category)
            return CATEGORY_ITEM;
        else
            return super.getItemViewType(position);

    }

    @Override
    public int getItemCount() {
        return recyclerViewItems.size();
    }

    private class VerticalViewHolder extends RecyclerView.ViewHolder {
        private RecyclerView horizontalRV;
        private TextView catTV, seeTV;

        public VerticalViewHolder(@NonNull View itemView) {
            super(itemView);
            horizontalRV = itemView.findViewById(R.id.horizontalRV);
            catTV = itemView.findViewById(R.id.catTV);
            seeTV = itemView.findViewById(R.id.seeMoreTV);
        }
    }

    //header holder
    private class HeaderHolder extends RecyclerView.ViewHolder {
        TextView texViewHeaderText, textViewCategory;
        ViewFlipper viewFlipper;
//        FrameLayout.LayoutParams params;

        public HeaderHolder(View itemView) {
            super(itemView);
            texViewHeaderText = itemView.findViewById(R.id.texViewHeaderText);
            textViewCategory = itemView.findViewById(R.id.textViewCategory);
            viewFlipper = itemView.findViewById(R.id.flipper);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewFlipper.LayoutParams.MATCH_PARENT, ViewFlipper.LayoutParams.MATCH_PARENT);
            Log.e("if header", "onBindViewHolder: " + sliders.size());
            for (SliderImage s : sliders) {
                setImageInFlipper(s, params);
            }
        }

        private void setImageInFlipper(SliderImage slider, LinearLayout.LayoutParams params) {
            ImageView image = new ImageView(context);
//            image.setBackgroundResource(R.drawable.image_round_border);
//            image.setClipToOutline(true);

            image.setLayoutParams(params);
            image.setImageBitmap(ImageHelper.getRoundedCornerBitmap(slider.getBitmap(),50));
            viewFlipper.addView(image);
        }
    }

    //footer holder
    private class FooterHolder extends RecyclerView.ViewHolder {
        TextView texViewQuote, textViewAuthor;
        ImageView imageViewFooter;

        public FooterHolder(View itemView) {
            super(itemView);
            texViewQuote = itemView.findViewById(R.id.texViewQuote);
            textViewAuthor = itemView.findViewById(R.id.textViewAuthor);
            imageViewFooter = itemView.findViewById(R.id.imageViewFooter);
        }
    }

    private class CategoryHolder extends RecyclerView.ViewHolder {
        LinearLayout linearLayout;
//        ImageView imageView;
        TextView textView;

        public CategoryHolder(View itemView) {
            super(itemView);
//            imageView = itemView.findViewById(R.id.catImg);
            textView = itemView.findViewById(R.id.catTxt);
            linearLayout = itemView.findViewById(R.id.imgLayout);

            DatabaseQuery query=new DatabaseQuery(context);
            List<Category> categories=query.getCategory();
            for (Category c: categories){
                setCatIcon(c);
            }

        }

        public void setCatIcon(final Category category) {
            View view = LayoutInflater.from(context).inflate(R.layout.single_cat, null, false);
            ImageView imageView = view.findViewById(R.id.catImg);
            TextView textView = view.findViewById(R.id.catTxt);
            textView.setTextColor(Color.parseColor("#ffffff"));
            textView.setMaxLines(2);
            textView.setFilters(new InputFilter[]{new InputFilter.LengthFilter(10)});
            textView.setText(category.getCategoryName());
            Log.e(TAG, "setCatIcon: "+category.getCategoryName() );
            LinearLayout.LayoutParams parms = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            parms.setMargins(20, 0, 20, 0);
            view.setLayoutParams(parms);

            imageView.setImageBitmap(ImageHelper.getRoundedCornerBitmap(category.getBitmap(),120));
//            imageView.setImageBitmap(category.getBitmap());
            linearLayout.addView(view);
            imageView.setId(category.getId());

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Toast.makeText(context, "clicked"+category.getId(), Toast.LENGTH_SHORT).show();
                    ((CategoryListener) context).onCatIconClick(category);
                }
            });
        }
    }


    public void updateList(List<VerticalModel> products) {

        List<RecyclerViewItem> recyclerViewItems = new ArrayList<>();

        Category category = new Category();
        recyclerViewItems.add(slider);

        recyclerViewItems.add(category);

        recyclerViewItems.addAll(products);

        Footer footer = new Footer("E-Sellers @copyright 2020",
                "", "https://cdn.pixabay.com/photo/2016/12/26/17/28/background-1932466_640.jpg");
        //add footer
        recyclerViewItems.add(footer);

        this.recyclerViewItems = recyclerViewItems;

        Log.e("", "updateList: " + recyclerViewItems.size());
        notifyDataSetChanged();
    }

    public void updateSlider(List<SliderImage> sliders) {
        List<RecyclerViewItem> recyclerViewItems = new ArrayList<>();
        this.slider = sliders.get(0);
        recyclerViewItems.add(slider);
        this.recyclerViewItems = recyclerViewItems;
        this.sliders = sliders;

        Log.e("mixed", "updateSlider: " + sliders.size());
        notifyDataSetChanged();
    }


    public interface MixedAdapterAction{
//        void onCatIconClick(int cid);
        void onProductImgClick();
//        void onSeeMoreClick();
    }

}
