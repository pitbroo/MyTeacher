import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ILesson } from '../lesson.model';
import { LessonService } from '../service/lesson.service';

@Component({
  templateUrl: './lesson-delete-dialog.component.html',
})
export class LessonDeleteDialogComponent {
  lesson?: ILesson;

  constructor(protected lessonService: LessonService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.lessonService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
