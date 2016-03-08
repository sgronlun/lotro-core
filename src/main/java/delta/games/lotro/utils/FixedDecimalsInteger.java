package delta.games.lotro.utils;

import delta.common.utils.NumericTools;

/**
 * An value with a fixed number of decimals (2).
 * @author DAM
 */
public class FixedDecimalsInteger extends Number
{
  private static final int DECIMALS = 2;
  private static final int FACTOR = (int)(Math.pow(10, DECIMALS));

  private int _value;

  @Override
  public int intValue()
  {
    return _value / FACTOR;
  }

  @Override
  public long longValue()
  {
    return intValue();
  }

  @Override
  public float floatValue()
  {
    return ((float)_value)/FACTOR;
  }

  @Override
  public double doubleValue()
  {
    return ((double)_value)/FACTOR;
  }

  /**
   * Default constructor.
   */
  public FixedDecimalsInteger()
  {
    _value=0;
  }

  /**
   * Constructor.
   * @param source Source value.
   */
  public FixedDecimalsInteger(FixedDecimalsInteger source)
  {
    _value=source._value;
  }

  /**
   * Constructor.
   * @param value Value.
   */
  public FixedDecimalsInteger(int value)
  {
    _value=value*FACTOR;
  }

  /**
   * Constructor.
   * @param value Value.
   */
  public FixedDecimalsInteger(float value)
  {
    _value=(int)(value*FACTOR);
  }

  /**
   * Build from an external representation string.
   * @param externalRepresentation External representation string.
   * @return A value.
   */
  public static FixedDecimalsInteger fromString(String externalRepresentation)
  {
    // TODO smarter string I/O
    FixedDecimalsInteger ret=new FixedDecimalsInteger();
    ret._value=NumericTools.parseInt(externalRepresentation,0);
    return ret;
  }

  /**
   * Add an integer value.
   * @param value Value to add.
   * @return this.
   */
  public FixedDecimalsInteger add(int value)
  {
    _value+=(value*FACTOR);
    return this;
  }

  /**
   * Add an integer value.
   * @param value Value to add.
   * @return this.
   */
  public FixedDecimalsInteger add(FixedDecimalsInteger value)
  {
    _value+=value._value;
    return this;
  }

  /**
   * Multiply by a factor value.
   * @param factor Value to use.
   * @return this.
   */
  public FixedDecimalsInteger multiply(FixedDecimalsInteger factor)
  {
    _value = (_value * factor._value) / FACTOR;
    return this;
  }

  /**
   * Multiply by a factor value.
   * @param factor Value to use.
   * @return this.
   */
  public FixedDecimalsInteger multiply(int factor)
  {
    _value = (_value * factor);
    return this;
  }

  /**
   * Get the internal value.
   * @return a value.
   */
  public int getInternalValue()
  {
    return _value;
  }

  @Override
  public String toString()
  {
    // TODO remove .0 if not needed
    return String.valueOf(floatValue());
  }
}
