import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CourseBuyComponentComponent } from './course-buy-component.component';

describe('CourseBuyComponentComponent', () => {
  let component: CourseBuyComponentComponent;
  let fixture: ComponentFixture<CourseBuyComponentComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ CourseBuyComponentComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(CourseBuyComponentComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
