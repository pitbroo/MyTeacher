import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { RankingService } from '../service/ranking.service';

import { RankingComponent } from './ranking.component';

describe('Component Tests', () => {
  describe('Ranking Management Component', () => {
    let comp: RankingComponent;
    let fixture: ComponentFixture<RankingComponent>;
    let service: RankingService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [RankingComponent],
      })
        .overrideTemplate(RankingComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(RankingComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(RankingService);

      const headers = new HttpHeaders().append('link', 'link;link');
      jest.spyOn(service, 'query').mockReturnValue(
        of(
          new HttpResponse({
            body: [{ id: 123 }],
            headers,
          })
        )
      );
    });

    it('Should call load all on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.rankings?.[0]).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
