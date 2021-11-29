import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IPoint, Point } from '../point.model';

import { PointService } from './point.service';

describe('Service Tests', () => {
  describe('Point Service', () => {
    let service: PointService;
    let httpMock: HttpTestingController;
    let elemDefault: IPoint;
    let expectedResult: IPoint | IPoint[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(PointService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        date: currentDate,
        value: 0,
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            date: currentDate.format(DATE_FORMAT),
          },
          elemDefault
        );

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a Point', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            date: currentDate.format(DATE_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            date: currentDate,
          },
          returnedFromService
        );

        service.create(new Point()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Point', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            date: currentDate.format(DATE_FORMAT),
            value: 1,
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            date: currentDate,
          },
          returnedFromService
        );

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a Point', () => {
        const patchObject = Object.assign(
          {
            value: 1,
          },
          new Point()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign(
          {
            date: currentDate,
          },
          returnedFromService
        );

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Point', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            date: currentDate.format(DATE_FORMAT),
            value: 1,
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            date: currentDate,
          },
          returnedFromService
        );

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a Point', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addPointToCollectionIfMissing', () => {
        it('should add a Point to an empty array', () => {
          const point: IPoint = { id: 123 };
          expectedResult = service.addPointToCollectionIfMissing([], point);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(point);
        });

        it('should not add a Point to an array that contains it', () => {
          const point: IPoint = { id: 123 };
          const pointCollection: IPoint[] = [
            {
              ...point,
            },
            { id: 456 },
          ];
          expectedResult = service.addPointToCollectionIfMissing(pointCollection, point);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Point to an array that doesn't contain it", () => {
          const point: IPoint = { id: 123 };
          const pointCollection: IPoint[] = [{ id: 456 }];
          expectedResult = service.addPointToCollectionIfMissing(pointCollection, point);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(point);
        });

        it('should add only unique Point to an array', () => {
          const pointArray: IPoint[] = [{ id: 123 }, { id: 456 }, { id: 61323 }];
          const pointCollection: IPoint[] = [{ id: 123 }];
          expectedResult = service.addPointToCollectionIfMissing(pointCollection, ...pointArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const point: IPoint = { id: 123 };
          const point2: IPoint = { id: 456 };
          expectedResult = service.addPointToCollectionIfMissing([], point, point2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(point);
          expect(expectedResult).toContain(point2);
        });

        it('should accept null and undefined values', () => {
          const point: IPoint = { id: 123 };
          expectedResult = service.addPointToCollectionIfMissing([], null, point, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(point);
        });

        it('should return initial array if no Point is added', () => {
          const pointCollection: IPoint[] = [{ id: 123 }];
          expectedResult = service.addPointToCollectionIfMissing(pointCollection, undefined, null);
          expect(expectedResult).toEqual(pointCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
