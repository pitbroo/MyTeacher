import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ITaskSolved } from '../task-solved.model';
import { TaskSolvedService } from '../service/task-solved.service';

@Component({
  templateUrl: './task-solved-delete-dialog.component.html',
})
export class TaskSolvedDeleteDialogComponent {
  taskSolved?: ITaskSolved;

  constructor(protected taskSolvedService: TaskSolvedService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.taskSolvedService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
