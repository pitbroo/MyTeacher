import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICourseUser } from '../course-user.model';

@Component({
  selector: 'jhi-course-user-detail',
  templateUrl: './course-user-detail.component.html',
})
export class CourseUserDetailComponent implements OnInit {
  courseUser: ICourseUser | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ courseUser }) => {
      this.courseUser = courseUser;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
