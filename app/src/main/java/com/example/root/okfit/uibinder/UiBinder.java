package com.example.root.okfit.uibinder;

/**
 * Ui线程与后台线程粘合工具
 * 实现Ui线程对后台线程的访问
 * <p>
 * Created by Lei Guoting on 17-3-19.
 */
public interface UiBinder<T> {
    int BEHAVIOR_LOADING_NORMAL = 0x03 << 25;
    int BEHAVIOR_SILENCE = 0x0f << 27;

    /**
     * Optional
     * <p>
     * {@link #workInBackground}执行前的预处理， e.g 显示加载进度
     */
    UiBinder<T> precedeInUi(Runnable precededTask);

    /**
     * Required
     */
    UiBinder<T> workInBackground(Work<T> work);

    /**
     * Optional
     */
    UiBinder<T> catchErrorInUi(Error error);

    /**
     * Optional
     */
    UiBinder<T> holdDataInUi(Hold<T> hold);

    /**
     * Optional
     */
    UiBinder<T> completeInUi(Runnable completedTask);

    /**
     * Must be invoked in Main thread
     * <p>
     * Default behavior {@link #apply(int)}
     */
    UiBinder<T> apply();

    /**
     * Must be invoked in Main thread
     * In Silence {@link #apply(int)}
     */
    UiBinder<T> applySilence();

    /**
     * Required
     * <p>
     * Must be invoked in Main thread
     */
    UiBinder<T> apply(int behavior);
}
