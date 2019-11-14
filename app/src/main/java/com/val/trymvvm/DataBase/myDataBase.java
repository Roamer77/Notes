package com.val.trymvvm.DataBase;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.val.trymvvm.Entities.Note;
import com.val.trymvvm.DAOs.NoteDao;

@Database(entities = {Note.class},version = 1)
public abstract class myDataBase extends RoomDatabase {
    private  static myDataBase instance;

    public  abstract NoteDao noteDao();

    public  static synchronized myDataBase getInstance(Context context){
        if(instance==null){
            instance= Room.databaseBuilder(context.getApplicationContext()
                    , myDataBase.class,"myDataBase")
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallBack)
                    .build();
        }
        return instance;
    }

    private  static RoomDatabase.Callback roomCallBack=new RoomDatabase.Callback(){
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new fillNoteTableWithData(instance).execute();
        }
    };
    //заполним данными нашу таблицу
    private  static class  fillNoteTableWithData extends AsyncTask<Void,Void,Void>{
        private  NoteDao noteDao;

        public fillNoteTableWithData(myDataBase dataBase) {
            this.noteDao = dataBase.noteDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            noteDao.insert(new Note("Title 1","Description 1",1));
            noteDao.insert(new Note("Title 2","Description 2",2));
            noteDao.insert(new Note("Title 3","Description 3",3));
            return null;
        }
    }
}
