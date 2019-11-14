package com.val.trymvvm.adaptors;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.val.trymvvm.Entities.Note;
import com.val.trymvvm.R;
import com.val.trymvvm.interfaces.OnItemClick;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewAdaptor extends ListAdapter<Note,RecyclerViewAdaptor.NoteHandler> {

    private OnItemClick listener; //свой интерфейс для нажатия

    public RecyclerViewAdaptor() {
        super(DIFFS_CALLBACK);
    }

    private  static  final DiffUtil.ItemCallback<Note> DIFFS_CALLBACK=new DiffUtil.ItemCallback<Note>() {
        @Override
        public boolean areItemsTheSame(@NonNull Note oldItem, @NonNull Note newItem) {
            return oldItem.getId()==newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Note oldItem, @NonNull Note newItem) {
            return oldItem.getTitle().equals(newItem.getTitle())&&
                    oldItem.getDescription().equals(newItem.getDescription())&&
                    oldItem.getPriority()==newItem.getPriority();
        }
    };
    //раздуваем элементами ресайклер
    @NonNull
    @Override
    public NoteHandler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.note_item_rw_layout, parent, false);
        return new NoteHandler(itemView);
    }


    //сетаем каждый элемент данными относительно его позиции
    @Override
    public void onBindViewHolder(@NonNull NoteHandler holder, int position) {
        Note note = getItem(position);
        holder.priorityTextView.setText(String.valueOf(note.getPriority()));
        holder.descriptionTextView.setText(note.getDescription());
        holder.tittleTextView.setText(note.getTitle());
    }


    public Note getNoteAt(int position) {
        return getItem(position);
    }

    //класс представления элемента ресайклер вью
    //обязхательно нужно унаследовтаься от  RecyclerView.ViewHolder
    class NoteHandler extends RecyclerView.ViewHolder {
        private TextView tittleTextView;
        private TextView descriptionTextView;
        private TextView priorityTextView;

        // тут есть возможность найти элементы 
        public NoteHandler(@NonNull final View itemView) {
            super(itemView);
            tittleTextView = itemView.findViewById(R.id.text_view_title);
            descriptionTextView = itemView.findViewById(R.id.text_view_description);
            priorityTextView = itemView.findViewById(R.id.text_view_priority);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition(); // вернёт позицию текущего элемента
                    if (position != RecyclerView.NO_POSITION && listener != null)
                        listener.onItemClick(getItem(position));
                }
            });
        }
    }

    //так у resycler view нет своего onClick listener метода нужнонаписать его самому
    public void setOnItemClickListener(OnItemClick listener) {
        this.listener = listener;

    }
}
