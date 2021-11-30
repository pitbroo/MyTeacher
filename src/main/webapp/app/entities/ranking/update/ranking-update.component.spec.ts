jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { RankingService } from '../service/ranking.service';
import { IRanking, Ranking } from '../ranking.model';

import { RankingUpdateComponent } from './ranking-update.component';

describe('Component Tests', () => {
  describe('Ranking Management Update Component', () => {
    let comp: RankingUpdateComponent;
    let fixture: ComponentFixture<RankingUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let rankingService: RankingService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [RankingUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(RankingUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(RankingUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      rankingService = TestBed.inject(RankingService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const ranking: IRanking = { id: 456 };

        activatedRoute.data = of({ ranking });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(ranking));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Ranking>>();
        const ranking = { id: 123 };
        jest.spyOn(rankingService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ ranking });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: ranking }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(rankingService.update).toHaveBeenCalledWith(ranking);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Ranking>>();
        const ranking = new Ranking();
        jest.spyOn(rankingService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ ranking });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: ranking }));
        saveSubject.complete();

        // THEN
        expect(rankingService.create).toHaveBeenCalledWith(ranking);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Ranking>>();
        const ranking = { id: 123 };
        jest.spyOn(rankingService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ ranking });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(rankingService.update).toHaveBeenCalledWith(ranking);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});
