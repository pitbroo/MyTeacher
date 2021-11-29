import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_FORMAT } from 'app/config/input.constants';
import { ILesson, Lesson } from '../lesson.model';

import { LessonService } from './lesson.service';

describe('Service Tests', () => {
  describe('Lesson Service', () => {
    let service: LessonService;
    let httpMock: HttpTestingController;
    let elemDefault: ILesson;
    let expectedResult: ILesson | ILesson[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(LessonService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        status: 'AAAAAAA',
        dateStart: currentDate,
        dateEnd: currentDate,
        classroomOrAddres: 'AAAAAAA',
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            dateStart: currentDate.format(DATE_FORMAT),
            dateEnd: currentDate.format(DATE_FORMAT),
          },
          elemDefault
        );

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a Lesson', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            dateStart: currentDate.format(DATE_FORMAT),
            dateEnd: currentDate.format(DATE_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            dateStart: currentDate,
            dateEnd: currentDate,
          },
          returnedFromService
        );

        service.create(new Lesson()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Lesson', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            status: 'BBBBBB',
            dateStart: currentDate.format(DATE_FORMAT),
            dateEnd: currentDate.format(DATE_FORMAT),
            classroomOrAddres: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            dateStart: currentDate,
            dateEnd: currentDate,
          },
          returnedFromService
        );

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a Lesson', () => {
        const patchObject = Object.assign({}, new Lesson());

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign(
          {
            dateStart: currentDate,
            dateEnd: currentDate,
          },
          returnedFromService
        );

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Lesson', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            status: 'BBBBBB',
            dateStart: currentDate.format(DATE_FORMAT),
            dateEnd: currentDate.format(DATE_FORMAT),
            classroomOrAddres: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            dateStart: currentDate,
            dateEnd: currentDate,
          },
          returnedFromService
        );

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a Lesson', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addLessonToCollectionIfMissing', () => {
        it('should add a Lesson to an empty array', () => {
          const lesson: ILesson = { id: 123 };
          expectedResult = service.addLessonToCollectionIfMissing([], lesson);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(lesson);
        });

        it('should not add a Lesson to an array that contains it', () => {
          const lesson: ILesson = { id: 123 };
          const lessonCollection: ILesson[] = [
            {
              ...lesson,
            },
            { id: 456 },
          ];
          expectedResult = service.addLessonToCollectionIfMissing(lessonCollection, lesson);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Lesson to an array that doesn't contain it", () => {
          const lesson: ILesson = { id: 123 };
          const lessonCollection: ILesson[] = [{ id: 456 }];
          expectedResult = service.addLessonToCollectionIfMissing(lessonCollection, lesson);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(lesson);
        });

        it('should add only unique Lesson to an array', () => {
          const lessonArray: ILesson[] = [{ id: 123 }, { id: 456 }, { id: 23127 }];
          const lessonCollection: ILesson[] = [{ id: 123 }];
          expectedResult = service.addLessonToCollectionIfMissing(lessonCollection, ...lessonArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const lesson: ILesson = { id: 123 };
          const lesson2: ILesson = { id: 456 };
          expectedResult = service.addLessonToCollectionIfMissing([], lesson, lesson2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(lesson);
          expect(expectedResult).toContain(lesson2);
        });

        it('should accept null and undefined values', () => {
          const lesson: ILesson = { id: 123 };
          expectedResult = service.addLessonToCollectionIfMissing([], null, lesson, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(lesson);
        });

        it('should return initial array if no Lesson is added', () => {
          const lessonCollection: ILesson[] = [{ id: 123 }];
          expectedResult = service.addLessonToCollectionIfMissing(lessonCollection, undefined, null);
          expect(expectedResult).toEqual(lessonCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
