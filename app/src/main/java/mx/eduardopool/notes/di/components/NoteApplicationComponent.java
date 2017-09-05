package mx.eduardopool.notes.di.components;

import javax.inject.Singleton;

import dagger.Component;
import mx.eduardopool.notes.activities.BaseActivity;
import mx.eduardopool.notes.di.modules.NoteApplicationModule;

/**
 * Note application component.
 * Created by epool on 11/16/15.
 */
@Singleton
@Component(modules = {NoteApplicationModule.class})
public interface NoteApplicationComponent {

    void inject(BaseActivity baseActivity);

}
