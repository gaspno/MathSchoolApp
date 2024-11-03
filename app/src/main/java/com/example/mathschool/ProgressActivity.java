package com.example.mathschool;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mathschool.adapter.ClassAdapter;
import com.example.mathschool.adapter.SubjectsAdapter;
import com.example.mathschool.adapter.TestAdapter;
import com.example.mathschool.adapter.model.ProgressClass;
import com.example.mathschool.adapter.model.ProgressSubject;
import com.example.mathschool.adapter.model.ProgressTest;
import com.example.mathschool.model.StudentRegister;
import com.example.mathschool.model.Subject;
import com.example.mathschool.model.Test;
import com.example.mathschool.services.ContentService;
import com.example.mathschool.services.ProgressService;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;


//essa activity gerencia a exibição do conteudo primero por assuntos depois mostra por aulas e testes disponiveis de cada assunto
public class ProgressActivity extends AppCompatActivity {

    private RecyclerView recyclerViewClass;
    private RecyclerView recyclerViewTest;
    private SubjectsAdapter subjectsAdapter;
    private ClassAdapter classAdapter;
    private ProgressBar progressBar;
    private String year;
    private List<Subject> listSubjects;
    private  AtomicReference<StudentRegister> register;
    private int actualPosition=-1;
    private String subjectActual="";
    private TextView userNameTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress_subject);
        TextView mainTextYear = findViewById(R.id.mainTextYear);
        userNameTextView=findViewById(R.id.userNameTextView);
        progressBar=findViewById(R.id.progressBarSubject);
        Drawable progressDrawable = progressBar.getProgressDrawable().mutate();
        progressDrawable.setColorFilter(Color.BLUE, PorterDuff.Mode.SRC_IN);
        progressBar.setProgressDrawable(progressDrawable);
        //obtem informação sobre o ano selecionado na activity anterior
        year= Objects.requireNonNull(getIntent().getExtras()).getString("year");
        recyclerViewClass =findViewById(R.id.recyclerViewProgress);
        recyclerViewTest=findViewById(R.id.recyclerViewProgress2);
        Thread loadInfo = new Thread(() -> {
            try {
                loadInfo();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        loadInfo.start();
        mainTextYear.setText(year);

    }
    private void loadInfo() throws IOException {

        AtomicBoolean isSetToken=new AtomicBoolean(false);
        AtomicBoolean isSetId=new AtomicBoolean(false);
        AtomicBoolean isReady=new AtomicBoolean(false);
        //recupera assuntos do back-end baseado no ano escolhido
        listSubjects= ContentService.getSubjects(year);
        if(FirebaseAuth.getInstance().getCurrentUser()==null){
            userNameTextView.setText("Convidado");
            subjectsAdapter=new SubjectsAdapter(listSubjects.stream().map(s->new ProgressSubject(0,s.getClassEntitySet().size(), 0,s.getTestEntitySet().size(),s.getImageUrl(),s.getName())).collect(Collectors.toList()), this,year);
            subjectsAdapter.setOnAdapterItemClicked((int position,String subject) -> {
            List<ProgressClass> list ;
            List<ProgressTest> listTests;
            list=(listSubjects.get(position).getClassEntitySet().stream().map(l->new ProgressClass(l.getName(),false,l.getId(),l.getContent_urls(),subject)).collect(Collectors.toList()));
            listTests=(listSubjects.get(position).getTestEntitySet().stream().map(t->new ProgressTest(false,t.getId(),t.getQuestionEntitySet(),subject)).collect(Collectors.toList()));
                ConstraintSet constraintSet = new ConstraintSet();
                ConstraintLayout parentLayout = findViewById(R.id.layoutConstraintBase);
                constraintSet.clone(parentLayout);
                constraintSet.connect(recyclerViewClass.getId(),ConstraintSet.BOTTOM,R.id.guideline6,ConstraintSet.BOTTOM);
                constraintSet.applyTo(parentLayout);
                classAdapter=new ClassAdapter(list,getApplicationContext(),year);
                TestAdapter adapter=new TestAdapter(listTests,getApplicationContext(),year);
                recyclerViewClass.setAdapter(classAdapter);
                recyclerViewClass.setLayoutManager(new LinearLayoutManager(getApplicationContext(),RecyclerView.VERTICAL,false));
                recyclerViewClass.setBackgroundColor(Color.YELLOW);
                recyclerViewTest.setAdapter(adapter);
                recyclerViewTest.setLayoutManager(new LinearLayoutManager(getApplicationContext(),RecyclerView.VERTICAL,false));
                recyclerViewTest.setBackgroundColor(Color.GREEN);
            });
        }else{
        register= new AtomicReference<>();
        userNameTextView.setText(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
        AtomicReference<String> firebaseToken = new AtomicReference<>();
        AtomicReference<Integer> studentId = new AtomicReference<>();
        FirebaseAuth.getInstance().getCurrentUser().getIdToken(true).addOnSuccessListener(getTokenResult ->{
            firebaseToken.set(getTokenResult.getToken());
            isSetToken.set(true);
        });
        FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getUid()).child("id_back-end").get().addOnSuccessListener(dataSnapshot ->
        {
            Long l;
            l= (Long) dataSnapshot.getValue();
            studentId.set(l.intValue());
            isSetId.set(true);
        });
        new Thread(() ->
            {
                //aguarde a obtenção do token e do id para fazer a requisição ao back-end
                while (!isSetId.get()||!isSetToken.get());
                try {
                    register.set(ProgressService.getStudentRegister(studentId.get(), firebaseToken.get()));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                isReady.set(true);
            }
        ).start();
        //aguarde o registro de aulas e teste concluidos esta recuperado
        while (!isReady.get());
        List<Boolean> completedList= listSubjects.stream().map(s->s.getClassEntitySet().stream().map(c->register.get().getLessonsId().contains(c.getId())).allMatch(a->a)).collect(Collectors.toList());
        runOnUiThread(() -> {
            progressBar.setMax(listSubjects.size());
            progressBar.setProgress((int) completedList.stream().filter(e->e).count());
            Log.d("Lista  : ",completedList.toString());
        });
        //usa a lista de subjects para criar uma lista de progresssubjects que registar o progresso de aulas e testes conluidos comparado ao total
        subjectsAdapter=new SubjectsAdapter(listSubjects.stream().map(s->new ProgressSubject((int) s.getClassEntitySet().stream().map(c->c.getId()).filter(f->register.get().getLessonsId().contains(f)).count(),s.getClassEntitySet().size(), (int)s.getTestEntitySet().stream().map(Test::getId).filter(f->register.get().getTestsId().contains(f)).count(),s.getTestEntitySet().size(),s.getImageUrl(),s.getName())).collect(Collectors.toList()), this,year);
        subjectsAdapter.setOnAdapterItemClicked((int position,String subject) -> {
            actualPosition=position;
            subjectActual=subject;
            setClassTestAdapters(position, subject, listSubjects, register);
        });
        //set o recycle view dos subjects
            runOnUiThread(() -> {
                recyclerViewClass.setAdapter(subjectsAdapter);
                recyclerViewClass.setLayoutManager(new GridLayoutManager(getApplicationContext(),2));
            });
        }
    }
    @Override
    protected void onRestart() {
        //atualiza o registro ao reiniciar a activity apos concluir uma aula ou teste
        if (actualPosition!=-1&& !Objects.equals(subjectActual, "") &&!subjectActual.isEmpty())
            setClassTestAdapters(actualPosition,subjectActual,listSubjects,register);
        super.onRestart();
    }

    private void setClassTestAdapters(int position, String subject, List<Subject> listSubjects, AtomicReference<StudentRegister> register) {
        AtomicReference<List<ProgressClass>> list = new AtomicReference<>();
        AtomicReference<List<ProgressTest>> listTests = new AtomicReference<>();
        AtomicBoolean isSetList=new AtomicBoolean(false);
        //criar listas separadas do progresso de aulas concluidas e teste concluidos
        new Thread(() -> {
            list.set(listSubjects.get(position).getClassEntitySet().stream().map(l->new ProgressClass(l.getName(), register.get().getLessonsId().contains(l.getId()),l.getId(),l.getContent_urls(), subject)).collect(Collectors.toList()));
            listTests.set(listSubjects.get(position).getTestEntitySet().stream().map(t->new ProgressTest(register.get().getTestsId().contains(t.getId()),t.getId(),t.getQuestionEntitySet(), subject)).collect(Collectors.toList()));
            isSetList.set(true);
        }).start();
        //aguarda lista estarem prontas
        while (!isSetList.get());
        //configura o recycler view do subject para as aulas e o dimunui, e expande um outro recycle view para os teste
        ConstraintSet constraintSet = new ConstraintSet();
        ConstraintLayout parentLayout = findViewById(R.id.layoutConstraintBase);
        constraintSet.clone(parentLayout);
        constraintSet.connect(recyclerViewClass.getId(),ConstraintSet.BOTTOM,R.id.guideline6,ConstraintSet.BOTTOM);
        constraintSet.applyTo(parentLayout);
        classAdapter=new ClassAdapter(list.get(),getApplicationContext(),year);
        TestAdapter adapter=new TestAdapter(listTests.get(),getApplicationContext(),year);
        recyclerViewClass.setAdapter(classAdapter);
        recyclerViewClass.setLayoutManager(new LinearLayoutManager(getApplicationContext(),RecyclerView.VERTICAL,false));
        recyclerViewClass.setBackgroundColor(Color.YELLOW);
        recyclerViewTest.setAdapter(adapter);
        recyclerViewTest.setLayoutManager(new LinearLayoutManager(getApplicationContext(),RecyclerView.VERTICAL,false));
        recyclerViewTest.setBackgroundColor(Color.GREEN);
    }
}