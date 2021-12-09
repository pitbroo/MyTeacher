import {Component, Input, OnInit} from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ITaskSolved } from '../task-solved.model';
import { TaskSolvedService } from '../service/task-solved.service';
import { TaskSolvedDeleteDialogComponent } from '../delete/task-solved-delete-dialog.component';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-task-solved',
  templateUrl: './task-solved.component.html',
})
export class TaskSolvedComponent implements OnInit {
  taskSolveds?: ITaskSolved[];
  isLoading = false;
  @Input()
  taskId?: any = "";

  constructor(protected taskSolvedService: TaskSolvedService, protected dataUtils: DataUtils, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.taskSolvedService.query(this.taskId).subscribe(
      (res: HttpResponse<ITaskSolved[]>) => {
        this.isLoading = false;
        this.taskSolveds = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: ITaskSolved): number {
    return item.id!;
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    return this.dataUtils.openFile(base64String, contentType);
  }

  delete(taskSolved: ITaskSolved): void {
    const modalRef = this.modalService.open(TaskSolvedDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.taskSolved = taskSolved;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
