package in.isotope.xquiethours;

import android.content.Context;
import android.preference.Preference;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ToggleButton;

public class DayOfWeekPreference extends Preference implements OnClickListener {

	private ToggleButton[] buttons = new ToggleButton[7];

	public DayOfWeekPreference(Context paramContext,
			AttributeSet paramAttributeSet) {
		super(paramContext, paramAttributeSet);
		setLayoutResource(R.layout.preference_days_of_week);
	}

	protected void onBindView(View view) {
		super.onBindView(view);

		ToggleButton sunday = (ToggleButton) view.findViewById(R.id.SUN);
		if (null != sunday && sunday instanceof ToggleButton) {
			sunday.setOnClickListener(this);
			buttons[0] = sunday;
		}

		ToggleButton monday = (ToggleButton) view.findViewById(R.id.MON);
		if (null != monday && monday instanceof ToggleButton) {
			monday.setOnClickListener(this);
			buttons[1] = monday;
		}

		ToggleButton tuesday = (ToggleButton) view.findViewById(R.id.TUE);
		if (null != tuesday && tuesday instanceof ToggleButton) {
			tuesday.setOnClickListener(this);
			buttons[2] = tuesday;
		}

		ToggleButton wednesday = (ToggleButton) view.findViewById(R.id.WED);
		if (null != wednesday && wednesday instanceof ToggleButton) {
			wednesday.setOnClickListener(this);
			buttons[3] = wednesday;
		}

		ToggleButton thursday = (ToggleButton) view.findViewById(R.id.THU);
		if (null != thursday && thursday instanceof ToggleButton) {
			thursday.setOnClickListener(this);
			buttons[4] = thursday;
		}

		ToggleButton friday = (ToggleButton) view.findViewById(R.id.FRI);
		if (null != friday && friday instanceof ToggleButton) {
			friday.setOnClickListener(this);
			buttons[5] = friday;
		}

		ToggleButton saturday = (ToggleButton) view.findViewById(R.id.SAT);
		if (null != saturday && saturday instanceof ToggleButton) {
			saturday.setOnClickListener(this);
			buttons[6] = saturday;
		}
		updateValue();
	}

	private void updateValue() {
		String saved = getPersistedString("");
		for (int i = 0; i < buttons.length; i++) {
			if (saved.contains(buttons[i].getText())) {
				buttons[i].setChecked(true);
			}
		}
	}

	@Override
	public void onClick(View v) {
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < buttons.length; i++) {
			if (buttons[i].isChecked()) {
				builder.append(buttons[i].getText());
				builder.append(";");
			}
		}
		persistString(builder.toString());
	}

	public void enableAll() {
		persistString("SUN;MON;TUE;WED;THU;FRI;SAT");
		updateValue();
	}
}
