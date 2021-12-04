import { Component, OnInit } from '@angular/core';
import {ICourse} from "../course.model";
import {CourseService} from "../service/course.service";
import {NgbActiveModal} from "@ng-bootstrap/ng-bootstrap";

@Component({
  selector: 'jhi-course-buy-component',
  templateUrl: './course-buy-component.component.html',
  styleUrls: ['./course-buy-component.component.scss']
})
export class CourseBuyComponentComponent  {

  course?: ICourse;

  constructor(protected courseService: CourseService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  buyConfirm(): void {
    this.activeModal.close('save');
    this.cancel();
  }
}
