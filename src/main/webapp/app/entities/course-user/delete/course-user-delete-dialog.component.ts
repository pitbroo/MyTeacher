import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ICourseUser } from '../course-user.model';
import { CourseUserService } from '../service/course-user.service';

@Component({
  templateUrl: './course-user-delete-dialog.component.html',
})
export class CourseUserDeleteDialogComponent {
  courseUser?: ICourseUser;

  constructor(protected courseUserService: CourseUserService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.courseUserService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
