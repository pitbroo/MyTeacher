import {Component, OnInit} from '@angular/core';
import {HttpResponse} from '@angular/common/http';
import {NgbModal} from '@ng-bootstrap/ng-bootstrap';

import {ICourseUser} from '../course-user.model';
import {CourseUserService} from '../service/course-user.service';
import {CourseUserDeleteDialogComponent} from '../delete/course-user-delete-dialog.component';
import {IUser} from "../../../admin/user-management/user-management.model";

@Component({
  selector: 'jhi-course-user',
  templateUrl: './course-user.component.html',
})
export class CourseUserComponent implements OnInit {
  courseUsers?: ICourseUser[];
  isLoading = false;


  constructor(protected courseUserService: CourseUserService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.courseUserService.query().subscribe(
      (res: HttpResponse<ICourseUser[]>) => {
        this.isLoading = false;
        this.courseUsers = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: ICourseUser): number {
    return item.id!;
  }

  delete(courseUser: ICourseUser): void {
    const modalRef = this.modalService.open(CourseUserDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.courseUser = courseUser;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
