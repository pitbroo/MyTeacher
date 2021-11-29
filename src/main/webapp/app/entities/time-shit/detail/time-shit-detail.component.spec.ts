import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { TimeShitDetailComponent } from './time-shit-detail.component';

describe('Component Tests', () => {
  describe('TimeShit Management Detail Component', () => {
    let comp: TimeShitDetailComponent;
    let fixture: ComponentFixture<TimeShitDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [TimeShitDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ timeShit: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(TimeShitDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(TimeShitDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load timeShit on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.timeShit).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});
