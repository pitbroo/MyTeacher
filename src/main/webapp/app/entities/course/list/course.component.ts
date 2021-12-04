import {Component, OnInit} from '@angular/core';
import {HttpResponse} from '@angular/common/http';
import {NgbModal} from '@ng-bootstrap/ng-bootstrap';

import {ICourse} from '../course.model';
import {CourseService} from '../service/course.service';
import {CourseDeleteDialogComponent} from '../delete/course-delete-dialog.component';
import {CourseUserService} from "../../course-user/service/course-user.service";
import {CourseUser, ICourseUser} from '../../course-user/course-user.model';
import {CourseBuyComponentComponent} from "../course-buy-component/course-buy-component.component";

@Component({
  selector: 'jhi-course',
  templateUrl: './course.component.html',
})
export class CourseComponent implements OnInit {
  courses?: ICourse[];
  myCourses?: ICourse[];
  isLoading = false;

  constructor(
    protected courseService: CourseService,
    protected modalService: NgbModal,
    protected courseUserService: CourseUserService
  ) {
  }

  loadAll(): void {
    this.loadAllMyCourses();
    this.isLoading = true;

    this.courseService.query().subscribe(
      (res: HttpResponse<ICourse[]>) => {
        this.isLoading = false;
        this.courses = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );

  }

  loadAllMyCourses(): void {
    this.isLoading = true;

    this.courseService.myCoursesQuery().subscribe(
      (res: HttpResponse<ICourse[]>) => {
        this.isLoading = false;
        this.myCourses = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: ICourse): number {
    return item.id!;
  }

  buyCourse(course: ICourse): void {
    const modalRef = this.modalService.open(CourseBuyComponentComponent);
    modalRef.componentInstance.course = course;
    modalRef.closed.subscribe(reason =>{
      if(reason === 'save'){
        const courseUser = new CourseUser();
        courseUser.course = course;
        this.courseUserService.create(courseUser).subscribe((x)=> {
          this.loadAll();
        });
      }
    })
  }


  delete(course: ICourse): void {
    const modalRef = this.modalService.open(CourseDeleteDialogComponent, {size: 'lg', backdrop: 'static'});
    modalRef.componentInstance.course = course;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }

}
