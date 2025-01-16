import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ChecklistendetailsComponent } from './checklistendetails.component';

describe('ChecklistendetailsComponent', () => {
  let component: ChecklistendetailsComponent;
  let fixture: ComponentFixture<ChecklistendetailsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ChecklistendetailsComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ChecklistendetailsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
