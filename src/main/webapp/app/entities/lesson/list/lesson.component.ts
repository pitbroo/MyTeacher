import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ILesson } from '../lesson.model';
import { LessonService } from '../service/lesson.service';
import { LessonDeleteDialogComponent } from '../delete/lesson-delete-dialog.component';

@Component({
  selector: 'jhi-lesson',
  templateUrl: './lesson.component.html',
})
export class LessonComponent implements OnInit {
  lessons?: ILesson[];
  isLoading = false;

  constructor(protected lessonService: LessonService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.lessonService.query().subscribe(
      (res: HttpResponse<ILesson[]>) => {
        this.isLoading = false;
        this.lessons = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: ILesson): number {
    return item.id!;
  }

  delete(lesson: ILesson): void {
    const modalRef = this.modalService.open(LessonDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.lesson = lesson;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
