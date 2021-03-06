package de.st_ddt.crazyutil.paramitrisable;

import de.st_ddt.crazyplugin.exceptions.CrazyException;
import de.st_ddt.crazyutil.paramitrisable.Paramitrisable.TypedParamitrisable;

public class StringParamitrisable extends TypedParamitrisable<String>
{

	public StringParamitrisable(final String defaultValue)
	{
		super(defaultValue);
	}

	@Override
	public void setParameter(final String parameter) throws CrazyException
	{
		value = parameter;
	}
}
