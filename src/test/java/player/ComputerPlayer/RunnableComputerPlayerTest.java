package player.ComputerPlayer;

import io.reactivex.Single;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

class RunnableComputerPlayerTest {

    @Test
    void validateObservablesThatCompleteAfterSingleEmit() {
        Subject<Integer> testSub = PublishSubject.create();

        Single<Integer> singleObs = getSingleValueObsFromSubject(testSub);

        AtomicInteger singleObs1Counter = new AtomicInteger(0);

        singleObs.subscribe((integer, throwable) -> {
            assertNull(throwable);
            assertNotNull(integer);

            singleObs1Counter.getAndIncrement();
            assertEquals(singleObs1Counter.get(), 1);
        });

        AtomicInteger singleObs2Counter = new AtomicInteger(0);
        singleObs.subscribe((integer, throwable) -> {
            assertNull(throwable);
            assertNotNull(integer);

            singleObs2Counter.getAndIncrement();
            assertEquals(singleObs2Counter.get(), 1);
        });

        AtomicInteger subCounter = new AtomicInteger(0);

        testSub.subscribe(counter -> {
            assertNotNull(counter);
            assertEquals(subCounter.getAndIncrement(), counter);
        }, Assertions::assertNull);

        int emitCount = 3;
        for (int i = 0; i < emitCount; i++) {
            testSub.onNext(i);
        }

        assertEquals(subCounter.get(), emitCount);
        assertEquals(singleObs1Counter.get(), 1);
        assertEquals(singleObs2Counter.get(), 1);
    }

    private Single<Integer> getSingleValueObsFromSubject(Subject<Integer> testSub) {
        return testSub.firstOrError();
    }

}