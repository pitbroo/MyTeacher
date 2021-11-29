import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ITimeShit } from '../time-shit.model';
import { TimeShitService } from '../service/time-shit.service';

@Component({
  templateUrl: './time-shit-delete-dialog.component.html',
})
export class TimeShitDeleteDialogComponent {
  timeShit?: ITimeShit;

  constructor(protected timeShitService: TimeShitService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.timeShitService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
