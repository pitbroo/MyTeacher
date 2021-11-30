import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { RankingDetailComponent } from './ranking-detail.component';

describe('Component Tests', () => {
  describe('Ranking Management Detail Component', () => {
    let comp: RankingDetailComponent;
    let fixture: ComponentFixture<RankingDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [RankingDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ ranking: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(RankingDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(RankingDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load ranking on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.ranking).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});
