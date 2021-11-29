import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ITaskSolved } from '../task-solved.model';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-task-solved-detail',
  templateUrl: './task-solved-detail.component.html',
})
export class TaskSolvedDetailComponent implements OnInit {
  taskSolved: ITaskSolved | null = null;

  constructor(protected dataUtils: DataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ taskSolved }) => {
      this.taskSolved = taskSolved;
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  previousState(): void {
    window.history.back();
  }
}
