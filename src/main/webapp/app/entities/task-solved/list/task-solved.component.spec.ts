import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { TaskSolvedService } from '../service/task-solved.service';

import { TaskSolvedComponent } from './task-solved.component';

describe('Component Tests', () => {
  describe('TaskSolved Management Component', () => {
    let comp: TaskSolvedComponent;
    let fixture: ComponentFixture<TaskSolvedComponent>;
    let service: TaskSolvedService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [TaskSolvedComponent],
      })
        .overrideTemplate(TaskSolvedComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(TaskSolvedComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(TaskSolvedService);

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
      expect(comp.taskSolveds?.[0]).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
